package org.ws.tanyunshou.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.ws.tanyunshou.dao.IAmountDao;
import org.ws.tanyunshou.vo.Amount;
import java.util.List;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author yinan
 * @date created in 下午3:16 18-12-26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@Transactional
public class AmountServiceImplTest {
    @Autowired
    private IAmountService amountService;

    @Test
    public void findAmountBySerialNo() {
        Amount amount = amountService.findAmountBySerialNo("weea1313ee12");
        Assert.assertEquals(new BigDecimal(100), amount.getMoney());
        Assert.assertEquals("main", amount.getThreadName());
    }

    @Test
    public void findAllAmounts() {
        List<Amount> list = amountService.findAllAmounts();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void insertAmount() {
        Amount amount = new Amount("weea1313ee12", new BigDecimal(100), Thread.currentThread().getName());
        amountService.insertAmount(amount);

    }

    @Test
    public void updateAmount() {

    }
}