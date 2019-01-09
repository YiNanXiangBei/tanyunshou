package org.ws.tanyunshou.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author yinan
 * @date 19-1-9
 */
@Component
public class MessageQueue {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);

    private BlockingDeque<MessageTask<Amount>> completeQueue = new LinkedBlockingDeque<>();

    public void put(MessageTask<Amount> task) throws InterruptedException {
        logger.info("get new task: {}", task);
        completeQueue.put(task);
    }

    public MessageTask<Amount> get() throws InterruptedException {
        logger.info("take new task ...");
        return completeQueue.take();
    }

}
