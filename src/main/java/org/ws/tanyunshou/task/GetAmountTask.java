package org.ws.tanyunshou.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.Callable;

/**
 * @author yinan
 * @date 19-1-1
 */
public class GetAmountTask implements Callable<Amount> {

    private static Logger logger = LoggerFactory.getLogger(GetAmountTask.class);

    private IAmountService amountService;

    private String serialNo;

    public GetAmountTask(IAmountService amountService, String serialNo) {
        this.amountService = amountService;
        this.serialNo = serialNo;
    }


    @Override
    public Amount call() {
        logger.info("get amount task, serial no: {}", serialNo);
        return amountService.findAmountBySerialNo(serialNo);
    }
}
