package org.ws.tanyunshou.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author yinan
 * @date created in 上午9:56 18-12-28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RabbitProducerTest {

    @Autowired
    private RabbitProducer producer;

    private static final String LUA_SCRIPT = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    @Test
    public void sendMessage() throws JsonProcessingException {
        Amount amount = new Amount("12ewe11", new BigDecimal(212), Thread.currentThread().getName());
        producer.sendMessage(amount);
    }


    @Test
    public void sendMoney() {
        BigDecimal bigDecimal = new BigDecimal("1000");
        producer.sendMoney(bigDecimal);
    }

    @Test
    public void sendSerialNo() {
        producer.sendSerialNo("e2da82c24e254bbaa324e4cb662f2ee6");
    }

    @Test
    public void save() {
        RedisClient client = RedisClient.create(RedisURI.create("redis://root1234@127.0.0.1:6379"));
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisCommands<String, String> commands = connection.sync();
        SetArgs px = SetArgs.Builder.nx().px(500000);
        String result =  commands.set("1111", "111", px);
        System.out.println(result);
    }

    @Test
    public void del() throws Exception{
        RedisClient client = RedisClient.create(RedisURI.create("redis://root1234@127.0.0.1:6379"));
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands<String, String> async = connection.async();
        String[] strings = {"AmountService_distributeLock_78155fa7a2084541b0a14affd2680434"};
        String value = "01d7d624d7e64e4e87939af9e08229b6";
        RedisFuture<Long> eval = async.eval(LUA_SCRIPT, ScriptOutputType.INTEGER, strings, value);
        Long aLong = eval.get();
        System.out.println("解锁结果-result: " + aLong);
    }
}