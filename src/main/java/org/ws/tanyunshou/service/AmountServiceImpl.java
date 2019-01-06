package org.ws.tanyunshou.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ws.tanyunshou.config.DataSourceNames;
import org.ws.tanyunshou.config.TargetDataSource;
import org.ws.tanyunshou.dao.IAmountDao;
import org.ws.tanyunshou.exception.InsufficientAmountException;
import org.ws.tanyunshou.redis.DistributeLock;
import org.ws.tanyunshou.redis.RedisConstant;
import org.ws.tanyunshou.util.CommonConstant;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yinan
 * @date created in 上午11:08 18-12-26
 */
@Service
@CacheConfig(cacheNames = RedisConstant.AMOUNT_CACHE_NAMES)
public class AmountServiceImpl implements IAmountService{
    private static Logger logger = LoggerFactory.getLogger(AmountServiceImpl.class);

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    private IAmountDao amountDao;

    @Cacheable(key = "#serialNo")
    @TargetDataSource(name = DataSourceNames.SLAVE)
    @Override
    public Amount findAmountBySerialNo(String serialNo) {
        logger.info("findAmountBySerialNo, serialNo: {}, thread name: {}", serialNo, Thread.currentThread().getName());
        return amountDao.findAmountBySerialNo(serialNo);
    }

    @TargetDataSource(name = DataSourceNames.SLAVE)
    @Override
    public List<Amount> findAllAmounts() {
        logger.info("findAllAmounts, thread name: {}", Thread.currentThread().getName());
        return amountDao.findAllAmounts();
    }

    /**
     * 插入数据进数据库
     * @Transactional 数据库事务
     * @CachePut redis缓存
     * @TragetDataSource 默认的数据源
     * @param amount
     * @return
     */
    @Transactional(value = "master_tr")
    @CachePut(key = "#amount.serialNo")
    @TargetDataSource
    @Override
    public Amount insertAmount(Amount amount) {
        logger.info("insertAmount, amount: {}, thread name: {}", amount.toString(), Thread.currentThread().getName());
        amountDao.insertAmount(amount);
        return amount;
    }

    /***
     * 更新数据
     * @CachePut 保存进redis
     * @TragetDataSource 默认的数据源
     * @param amount
     * @return
     */
    @DistributeLock(name = "AmountService_distributeLock", value = "#amount.serialNo")
    @CachePut(key = "#amount.serialNo")
    @TargetDataSource
    @Override
    public Amount updateAmount(Amount amount) throws InsufficientAmountException {
        try {
            readWriteLock.writeLock().lock();
            Amount oldAmount = amountDao.findAmountBySerialNo(amount.getSerialNo());
            BigDecimal money = amount.getMoney().add(oldAmount.getMoney());
            if (money.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientAmountException(CommonConstant.INSUFFICIENT_AMOUNT);
            }
            amount.setMoney(money);
            amount.setThreadName(Thread.currentThread().getName());
            logger.info("updateAmount, amount: {}, thread name: {}", amount.toString(), Thread.currentThread().getName());
            reallyUpdateAmount(amount);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return amount;
    }

    /**
     * 独立事务
     * 解决事务与锁机制无法一起使用的问题
     * @param amount
     * @return
     */
    @Transactional(value = "master_tr", rollbackFor = SQLException.class)
    @TargetDataSource
    public void reallyUpdateAmount(Amount amount) {
        amountDao.updateAmount(amount);
    }
}