package org.ws.tanyunshou.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author yinan
 * @date created in 下午1:09 18-12-26
 */
@Component
@RabbitListener(queues = RabbitConstant.AMOUNT_QUEUE)
public class RabbitProducer {

    private static Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    @RabbitHandler
    public void process(Object object) {
        logger.debug("get message: {}", object.toString());
    }

}
