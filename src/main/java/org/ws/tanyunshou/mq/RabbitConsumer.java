package org.ws.tanyunshou.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yinan
 * @date created in 下午1:09 18-12-26
 */
@Component
public class RabbitConsumer {

    private static Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Object object) {
        logger.debug("send message: {}", object.toString());
        rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE, RabbitConstant.AMOUNT_ROUTING_KEY, object);
    }

}
