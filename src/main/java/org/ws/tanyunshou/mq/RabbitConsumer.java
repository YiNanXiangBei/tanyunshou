package org.ws.tanyunshou.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.vo.Amount;

import java.io.IOException;

/**
 * @author yinan
 * @date created in 下午1:09 18-12-26
 */
@Component
@RabbitListener(queues = RabbitConstant.AMOUNT_QUEUE, containerFactory = "multiListenerContainer")
public class RabbitConsumer {
    private static Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    @RabbitHandler
    public void process(Amount amount) {
        logger.debug("get message: {}", amount.toString());
    }

}
