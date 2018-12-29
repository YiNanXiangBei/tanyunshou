package org.ws.tanyunshou.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.ws.tanyunshou.config.TransactionConfig;
import org.ws.tanyunshou.dao.IAmountDao;
import org.ws.tanyunshou.vo.Amount;
import java.util.List;

import java.math.BigDecimal;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author yinan
 * @date created in 下午3:16 18-12-26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableTransactionManagement
@EnableCaching
public class AmountServiceImplTest {
    @Autowired
    private IAmountService amountService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

    @Rollback
    @Test
    public void insertAmount() {
        Amount amount = new Amount("weea1313ee12", new BigDecimal(100), Thread.currentThread().getName());
        amountService.insertAmount(amount);

    }

    @Rollback
    @Test
    public void updateAmount() {
        Amount amount = new Amount("weea1313ee12", new BigDecimal(101), Thread.currentThread().getName());
        amountService.updateAmount(amount);
    }

    @Test
    public void testAmount() throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 10,
                5, TimeUnit.SECONDS, new SynchronousQueue<>(),
                r -> new Thread(r, "amount_pool_" + r.hashCode()));
        UpdateTaskTest task1 = new UpdateTaskTest(amountService, new BigDecimal(10), "weea1313ee12");
        UpdateTaskTest task2 = new UpdateTaskTest(amountService, new BigDecimal(-1), "weea1313ee12");
        UpdateTaskTest task3 = new UpdateTaskTest(amountService, new BigDecimal(5), "weea1313ee12");
        UpdateTaskTest task4 = new UpdateTaskTest(amountService, new BigDecimal(-6), "weea1313ee12");
        UpdateTaskTest task5 = new UpdateTaskTest(amountService, new BigDecimal(4), "weea1313ee12");
        UpdateTaskTest task6 = new UpdateTaskTest(amountService, new BigDecimal(-2), "weea1313ee12");
        UpdateTaskTest task7 = new UpdateTaskTest(amountService, new BigDecimal(8), "weea1313ee12");
        UpdateTaskTest task8 = new UpdateTaskTest(amountService, new BigDecimal(1), "weea1313ee12");
        UpdateTaskTest task9 = new UpdateTaskTest(amountService, new BigDecimal(-10), "weea1313ee12");
        UpdateTaskTest task10 = new UpdateTaskTest(amountService, new BigDecimal(5), "weea1313ee12");
        poolExecutor.execute(task1);
        poolExecutor.execute(task2);
        poolExecutor.execute(task3);
        poolExecutor.execute(task4);
        poolExecutor.execute(task5);
        poolExecutor.execute(task6);
        poolExecutor.execute(task7);
        poolExecutor.execute(task8);
        poolExecutor.execute(task9);
        poolExecutor.execute(task10);
        TimeUnit.SECONDS.sleep(60);

    }


}