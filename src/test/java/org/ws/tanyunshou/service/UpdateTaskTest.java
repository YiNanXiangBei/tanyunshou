package org.ws.tanyunshou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date 18-12-28
 */
public class UpdateTaskTest implements Runnable {

    private IAmountService amountService;

    private BigDecimal money;

    private String serialNo;


    public UpdateTaskTest(IAmountService amountService, BigDecimal money, String serialNo) {
        this.amountService = amountService;
        this.money = money;
        this.serialNo = serialNo;
    }


    @Override
    public void run() {
        System.out.println(this.serialNo);
        amountService.updateAmount(new Amount(this.serialNo, this.money, Thread.currentThread().getName()));
    }
}
