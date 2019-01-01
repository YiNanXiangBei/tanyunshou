package org.ws.tanyunshou.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.vo.Amount;

/**
 * @author yinan
 * @date 19-1-1
 */
public class UpdateAmountTask implements Runnable{

    private IAmountService amountService;

    private static Logger logger = LoggerFactory.getLogger(UpdateAmountTask.class);

    private Amount amount;

    public UpdateAmountTask(IAmountService amountService, Amount amount) {
        this.amountService = amountService;
        this.amount = amount;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        logger.info("update amount task , amount: {}, thread name: {}", amount.toString(), threadName);
        amount.setThreadName(threadName);
        amountService.updateAmount(amount);
    }
}
