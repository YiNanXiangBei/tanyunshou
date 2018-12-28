package org.ws.tanyunshou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date 18-12-28
 */
public class UpdateTaskTest implements Runnable {

    @Autowired
    private IAmountService amountService;

    public BigDecimal money;

    public String serialNo;


    public UpdateTaskTest(BigDecimal money, String serialNo) {
        this.money = money;
        this.serialNo = serialNo;
    }


    @Override
    public void run() {
        System.out.println(this.serialNo);
        amountService.updateAmount(new Amount(this.serialNo, this.money, Thread.currentThread().getName()));
    }
}
