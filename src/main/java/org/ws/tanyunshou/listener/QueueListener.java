package org.ws.tanyunshou.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.task.ApplicationEventTask;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.HttpRequestMap;
import org.ws.tanyunshou.util.MessageQueue;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinan
 * @date 19-1-9
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private HttpRequestMap hashMap;

    @Autowired
    private MessageQueue queue;

    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 2,
            10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1),
            r -> new Thread(r, "aplication_event_pool_" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        poolExecutor.execute(new ApplicationEventTask(queue, hashMap));
    }
}
