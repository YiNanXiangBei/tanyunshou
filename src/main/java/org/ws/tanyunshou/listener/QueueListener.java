package org.ws.tanyunshou.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.MessageQueue;
import org.ws.tanyunshou.vo.Amount;

/**
 * @author yinan
 * @date 19-1-9
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MessageQueue queue;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(() -> {
            while (true) {
                try {
                    MessageTask<Amount> task = queue.get();
                    ResponseMessage message = new ResponseMessage(CommonConstant.SUCCESS_RESPONSE,
                            task.getMessage(), CommonConstant.SUCCESS_REQUEST_MESSAGE);
                            task.getResult().setResult(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
