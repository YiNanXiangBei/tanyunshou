package org.ws.tanyunshou.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.ws.tanyunshou.vo.Amount;

import java.time.Duration;

/**
 * @author yinan
 * @date 18-12-25
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<String, Amount> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Amount> redisTemplate = new RedisTemplate<>();
        this.initRedisTemplate(redisTemplate, redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Amount> serializer = new Jackson2JsonRedisSerializer<>(Amount.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(objectMapper);


        //设置缓存有效期时间为1小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(redisCacheConfiguration)
                .build();

    }

    private void initRedisTemplate(RedisTemplate<String, Amount> redisTemplate, RedisConnectionFactory factory) {
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Amount> serializer = new Jackson2JsonRedisSerializer<>(Amount.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(objectMapper);


        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(factory);
    }

}
