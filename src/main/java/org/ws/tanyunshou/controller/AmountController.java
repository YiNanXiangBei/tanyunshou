package org.ws.tanyunshou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ws.tanyunshou.mq.RabbitProducer;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.task.GetAmountTask;
import org.ws.tanyunshou.task.IncreaseAmountTask;
import org.ws.tanyunshou.task.UpdateAmountTask;
import org.ws.tanyunshou.util.CommonTools;
import org.ws.tanyunshou.util.HttpRequestMap;
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

    @Autowired
    private RabbitProducer producer;


    @PostMapping(value = "/add")
    public void addAmount(BigDecimal money) {
        producer.sendMoney(money);
    }

    @PostMapping(value = "/update")
    public void updateAmount(Amount amount) {
        producer.sendMessage(amount);
    }

    @GetMapping(value = "/get")
    public Amount findAmount(String serialNo) {
        producer.sendSerialNo(serialNo);
        try {
            return HttpRequestMap.take(serialNo);
        } catch (InterruptedException e) {
            logger.error("can not get amount, serial no: {}", serialNo);
        }
        return null;
    }

}
