package org.ws.tanyunshou.redis;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.CommonTools;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author yinan
 * @date 19-1-6
 */
//@Component
public class RedisDistributeLock extends AbstractDistributeLockImpl {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributeLock.class);

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    private ThreadLocal<String> lockFlag = new ThreadLocal<>();

    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public RedisDistributeLock() {
        super();
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMills) {
        boolean result = setRedis(key, expire);
        while (!result && (retryTimes --) > 0) {

            try {
                logger.debug("lock failed, retrying ... {}", retryTimes);
                TimeUnit.SECONDS.sleep(sleepMills);
            } catch (InterruptedException e) {
                logger.error("locked error, because: {}", e.toString());
                return false;
            }
            result = setRedis(key, expire);
        }

        return result;
    }

    /**
     * 在获取锁的时候就能够保证设置 Redis 值和过期时间的原子性，避免前面提到的两次 Redis 操作期间出现意外而导致的锁不能释放的问题。但是这样还是可能会存在一个问题，考虑如下的场景顺序：
     *
     * 1. 线程T1获取锁
     * 2. 线程T1执行业务操作，由于某些原因阻塞了较长时间
     * 3. 锁自动过期，即锁自动释放了
     * 4. 线程T2获取锁
     * 5. 线程T1业务操作完毕，释放锁（其实是释放的线程T2的锁）
     * 6. 按照这样的场景顺序，线程T2的业务操作实际上就没有锁提供保护机制了。所以，每个线程释放锁的时候只能释放自己的锁，即锁必须要有一个拥有者的标记，并且也需要保证释放锁的原子性操作。
     *
     * 因此在获取锁的时候，可以生成一个随机不唯一的串放入当前线程中，然后再放入 Redis 。释放锁的时候先判断锁对应的值是否与线程中的值相同，相同时才做删除操作
     *
     * @param key redis key
     * @return 是否释放锁成功
     */
    @Override
    public boolean releaseLock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
//            List<String> keys = new ArrayList<>();
//            keys.add(key);
//            List<String> args = new ArrayList<>();
//            args.add(lockFlag.get());
//            String[] keys = {key};
//            String value = lockFlag.get();
            //使用lua脚本删除redis中匹配的value的key 可以避免由于执行过长时间而导致redis锁自动过期的时候误删其它线程的锁
            //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
                Object nativeConnection = redisConnection.getNativeConnection();
                //集群模式和单机模式虽然执行脚本相同，但是由于没有共同接口，所以只能分开执行
                //集群模式
                RedisAsyncCommands<byte[], byte[]> asyncCommands = (RedisAsyncCommands<byte[], byte[]>) nativeConnection;
                RedisSerializer<String> objectRedisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                byte[] keys = objectRedisSerializer.serialize(key);
                byte[] values = objectRedisSerializer.serialize(lockFlag.get());
//                StatefulRedisConnection connection = asyncCommands.getStatefulConnection();
                RedisFuture<Long> future = asyncCommands.eval(UNLOCK_LUA, ScriptOutputType.INTEGER,  keys, values);
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return 0L;
            });
            logger.debug("release lock val : {}", result);
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("release locked error, occured an exception {}", e);
        } finally {
            lockFlag.remove();
        }
        return false;
    }

    private boolean setRedis(String key, long expire) {
        try {
            String result = redisTemplate.execute((RedisCallback<String>) redisConnection -> {
                Object nativeConnection =  redisConnection.getNativeConnection();
                @SuppressWarnings("unchecked")
                RedisSerializer<String> objectRedisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
                byte[] keyByte = objectRedisSerializer.serialize(key);
                String uuid = CommonTools.getUUID();
                logger.debug("uuid is : {}", uuid);
                byte[] valueByte = objectRedisSerializer.serialize(uuid);
                lockFlag.set(uuid);
                RedisAsyncCommands<byte[], byte[]> asyncCommands = (RedisAsyncCommands<byte[], byte[]>) nativeConnection;
                Future<String> future =  asyncCommands.set(keyByte, valueByte, SetArgs.Builder.nx().px(expire));
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return "OK".equalsIgnoreCase(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception {}", e);
        }
        return false;

    }


}
