package org.ws.tanyunshou.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.exception.InsufficientAmountException;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.task.IncreaseAmountTask;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.util.*;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * @author yinan
 * @date created in 下午1:09 18-12-26
 */
@Component
public class RabbitConsumer {
    private static Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    @Autowired
    private HttpRequestMap hashMap;

    @Autowired
    private HttpRequestSerialNoMap serialNoMap;

    @Autowired
    private IAmountService amountService;

    @Autowired
    private MessageQueue queue;

    private ThreadPoolExecutor incPoolExec = new ThreadPoolExecutor(10, 15,
            1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(30), r -> new Thread(r, "inc_amount_pool_" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor updatePoolExec = new ThreadPoolExecutor(10, 15,
            10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(30),
            r -> new Thread(r, "update_amount_pool_" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor getPoolExec = new ThreadPoolExecutor(8, 10, 10,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(50),
            r -> new Thread(r, "get_amount_pool_" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @RabbitListener(queues = RabbitConstant.AMOUNT_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    public void process(MessageTask<Amount> task) {

        CompletableFuture
                .supplyAsync(() -> {
                    logger.info("update amount: {}, queue name: {}, current thread: {}, queue size: {}", task.toString(),
                            RabbitConstant.AMOUNT_QUEUE, Thread.currentThread().getName(), hashMap.size());
                    try {
                        return amountService.updateAmount(task.getMessage());
                    } catch (InsufficientAmountException e) {
                        logger.error("can not update amount, because {}", e.toString());
                    }
                    return null;
                }, updatePoolExec)
                .thenAccept(amount1 -> {
                    try {
                        if (amount1 == null) {
                            task.setMessage(CommonConstant.AMOUNT);
                        } else {
                            task.setMessage(amount1);
                        }
                        queue.put(task);
                    } catch (InterruptedException e) {
                        logger.error("HttpRequestMap put val error: {}", e.toString());
                    }
                });

    }


    @RabbitListener(queues = RabbitConstant.MONEY_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    public void processMoney(BigDecimal money) {
        logger.info("get money: {}, queue name: {}, current thread: {}", money,
                RabbitConstant.MONEY_QUEUE, Thread.currentThread().getName());
        incPoolExec.execute(new IncreaseAmountTask(amountService, money, CommonTools.getUUID()));
    }

    @RabbitListener(queues = RabbitConstant.SERIAL_NO_QUEUE, containerFactory = "multiListenerContainer")
    @RabbitHandler
    public void processSerialNo(String serialNo) {
        MessageTask<Amount> task = new MessageTask<>()
        CompletableFuture
                .supplyAsync(() -> {
                    logger.info("get serial no: {}, queue name: {}, current thread: {}, queue size: {}", serialNo,
                            RabbitConstant.SERIAL_NO_QUEUE, Thread.currentThread().getName(), serialNoMap.size());
                    return amountService.findAmountBySerialNo(serialNo); }
                    , getPoolExec)
                .thenAccept(amount -> {
                    try {
                        serialNoMap.put(serialNo, amount);
                    } catch (Exception e) {
                        logger.error("HttpRequestSerialNoMap put val error: {}", e.toString());
                    }
                });
    }
}
