package org.ws.tanyunshou.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date 19-1-1
 */
public class IncreaseAmountTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(IncreaseAmountTask.class);

    private IAmountService amountService;

    private BigDecimal money;

    private String serialNo;

    public IncreaseAmountTask(IAmountService amountService, BigDecimal money, String serialNo) {
        this.amountService = amountService;
        this.money = money;
        this.serialNo = serialNo;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        logger.info("increase amount task , money: {}, serialNo: {}, thread name: {}", money, serialNo, threadName);
        amountService.insertAmount(new Amount(serialNo, money, threadName));
    }
}
