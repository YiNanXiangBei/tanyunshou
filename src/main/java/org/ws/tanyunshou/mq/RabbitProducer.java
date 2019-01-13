package org.ws.tanyunshou.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date created in 下午1:09 18-12-26
 */
@Component
public class RabbitProducer {

    private static Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageTask<Amount> task) {
        logger.debug("send message: {}", task);
        rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE,
                RabbitConstant.AMOUNT_ROUTING_KEY, task);
    }

    public void sendMoney(BigDecimal money) {
        logger.debug("send money: {}", money);
        rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE, RabbitConstant.MONEY_ROUTING_KEY, money);
    }

    public void sendSerialNo(MessageTask<String> task) {
        logger.debug("send serial no task: {}", task);
        rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE, RabbitConstant.SERIAL_ROUTING_KEY, task);
    }

}
