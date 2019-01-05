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
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.CommonTools;
import org.ws.tanyunshou.util.HttpRequestMap;
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
    private IAmountService amountService;

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
    public void process(Amount amount) {

        CompletableFuture
                .supplyAsync(() -> {
                    logger.info("update amount: {}, queue name: {}, current thread: {}", amount.toString(),
                            RabbitConstant.AMOUNT_QUEUE, Thread.currentThread().getName());
                    try {
                        return amountService.updateAmount(amount);
                    } catch (InsufficientAmountException e) {
                        logger.error("can not update amount, because {}", e.toString());
                    }
                    return null;
                }, updatePoolExec)
                .thenAccept(amount1 -> {
                    try {
                        if (amount1 == null) {
                            HttpRequestMap.put("#" + amount.getSerialNo(), CommonConstant.AMOUNT);
                        } else {
                            HttpRequestMap.put("#" + amount.getSerialNo(), amount1);
                        }
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
        CompletableFuture
                .supplyAsync(() -> {
                    logger.info("get serial no: {}, queue name: {}, current thread: {}", serialNo,
                            RabbitConstant.SERIAL_NO_QUEUE, Thread.currentThread().getName());
                    return amountService.findAmountBySerialNo(serialNo); }
                    , getPoolExec)
                .thenAccept(amount -> {
                    try {
                        HttpRequestMap.put(serialNo, amount);
                    } catch (InterruptedException e) {
                        logger.error("HttpRequestMap put val error: {}", e.toString());
                    }
                });
    }
}
