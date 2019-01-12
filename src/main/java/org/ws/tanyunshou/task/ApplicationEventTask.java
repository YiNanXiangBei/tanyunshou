package org.ws.tanyunshou.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.HttpRequestMap;
import org.ws.tanyunshou.util.MessageQueue;
import org.ws.tanyunshou.vo.Amount;

/**
 * @author yinan
 * @date created in 下午1:50 19-1-12
 */
public class ApplicationEventTask implements Runnable {


    private MessageQueue queue;

    private HttpRequestMap hashMap;

    private static Logger logger = LoggerFactory.getLogger(ApplicationEventTask.class);

    public ApplicationEventTask(MessageQueue queue, HttpRequestMap hashMap) {
        this.queue = queue;
        this.hashMap = hashMap;
    }

    @Override
    public void run() {
        MessageTask<Amount> task;
        try {
            while (true) {
                task = queue.get();
                logger.debug("get task success: {}", task);
                ResponseMessage message = new ResponseMessage(CommonConstant.SUCCESS_RESPONSE,
                        task.getMessage(), CommonConstant.SUCCESS_REQUEST_MESSAGE);
                DeferredResult<ResponseMessage> result = hashMap.take(task.getCode());
                result.setResult(message);
            }
        } catch (InterruptedException e) {
            logger.error("queue get error: {}", e.toString());
        }

    }
}
