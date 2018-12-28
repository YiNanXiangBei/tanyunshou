package org.ws.tanyunshou.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Test
    public void sendMessage() throws JsonProcessingException {
        Amount amount = new Amount("12ewe11", new BigDecimal(212), Thread.currentThread().getName());
        producer.sendMessage(amount);
    }
}