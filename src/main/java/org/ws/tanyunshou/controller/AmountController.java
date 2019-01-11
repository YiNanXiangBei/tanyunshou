package org.ws.tanyunshou.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.mq.RabbitProducer;
import org.ws.tanyunshou.service.IAmountService;
import org.ws.tanyunshou.task.MessageTask;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.util.HttpRequestMap;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

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
    private HttpRequestMap hashMap;

    @Autowired
    private RabbitProducer producer;


    @PostMapping(value = "/add")
    public void addAmount(BigDecimal money) {
        producer.sendMoney(money);
    }

    @PostMapping(value = "/update")
    public DeferredResult<ResponseMessage> updateAmount(Amount amount) {
        DeferredResult<ResponseMessage> result = new DeferredResult<>(100000L);
        String code = String.valueOf(result.hashCode());
        hashMap.put(code, result);
        MessageTask<Amount> task = new MessageTask<>(code, amount);
        producer.sendMessage(task);

        ResponseMessage message = new ResponseMessage(CommonConstant.INTERNAL_SERVER_ERROR,
                null, CommonConstant.SERVICE_UNAVAILABLE_MESSAGE);

//        result.onTimeout(() -> {
//            task.setTimeout(true);
//            result.setErrorResult(message);
//        });


//        try {
//            Amount newAmount = HttpRequestMap.take("#" + amount.getSerialNo());
//            message.setData(newAmount);
//            if (newAmount == CommonConstant.AMOUNT) {
//                message.setCode(CommonConstant.SUCCESS_RESPONSE);
//                message.setMessage(CommonConstant.INSUFFICIENT_AMOUNT);
//            } else if (newAmount != null){
//                message.setCode(CommonConstant.SUCCESS_RESPONSE);
//                message.setMessage(CommonConstant.SUCCESS_REQUEST_MESSAGE);
//            }
//            return message;
//        } catch (InterruptedException e) {
//            logger.error("can not get new amount, serial no: {}", amount.getSerialNo());
//        }

        return result;
    }

    @GetMapping(value = "/get")
    public ResponseMessage findAmount(String serialNo) {
        producer.sendSerialNo(serialNo);
        ResponseMessage message = new ResponseMessage(CommonConstant.INTERNAL_SERVER_ERROR,
                null, CommonConstant.SERVICE_UNAVAILABLE_MESSAGE);
        try {
//            Amount amount = HttpRequestMap.take(serialNo);
            Amount amount = null;
            message.setCode(CommonConstant.SUCCESS_RESPONSE);
            message.setData(amount);
            message.setMessage(CommonConstant.SUCCESS_REQUEST_MESSAGE);
            return message;
        } catch (Exception e) {
            logger.error("can not get amount, serial no: {}", serialNo);
        }
        return message;
    }

}
