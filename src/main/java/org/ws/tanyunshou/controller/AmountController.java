package org.ws.tanyunshou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.task.GetAmountTask;
import org.ws.tanyunshou.task.IncreaseAmountTask;
import org.ws.tanyunshou.task.UpdateAmountTask;
import org.ws.tanyunshou.util.CommonTools;
import org.ws.tanyunshou.vo.Amount;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * @author yinan
 * @date created in 上午11:08 18-12-26
 */
@RestController
public class AmountController {

    private static Logger logger = LoggerFactory.getLogger(AmountController.class);

    @Autowired
    private IAmountService amountService;

    private ThreadPoolExecutor incPoolExec = new ThreadPoolExecutor(10, 15,
            1, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50, true), r -> new Thread(r, "inc_amount_pool_" + r.hashCode()),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor updatePoolExec = new ThreadPoolExecutor(10, 15,
            10, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), r -> new Thread(r, "update_amount_pool_" + r.hashCode()));

    private ThreadPoolExecutor getPoolExec = new ThreadPoolExecutor(20, 30, 10,
            TimeUnit.SECONDS, new LinkedBlockingDeque<>(50), r -> new Thread(r, "get_amount_pool_" + r.hashCode()));

    @PostMapping(value = "/add")
    public void addAmount(BigDecimal money) {
        incPoolExec.execute(new IncreaseAmountTask(amountService, money, CommonTools.getUUID()));
    }

    @PostMapping(value = "/update")
    public void updateAmount(Amount amount) {
        updatePoolExec.execute(new UpdateAmountTask(amountService, amount));
    }

    @GetMapping(value = "/get")
    public Amount findAmount(String serialNo) {
        Future<Amount> result = getPoolExec.submit(new GetAmountTask(amountService, serialNo));
        while (!result.isDone()) {

        }
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("can not get amount now, for serial no is : {}", serialNo);
        }
        return null;
    }

}
