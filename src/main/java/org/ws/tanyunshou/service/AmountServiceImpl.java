package org.ws.tanyunshou.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.ws.tanyunshou.config.DataSourceNames;
import org.ws.tanyunshou.config.TargetDataSource;
import org.ws.tanyunshou.dao.IAmountDao;
import org.ws.tanyunshou.redis.RedisConstant;
import org.ws.tanyunshou.vo.Amount;

import java.util.List;

/**
 * @author yinan
 * @date created in 上午11:08 18-12-26
 */
@Service
@CacheConfig(cacheNames = RedisConstant.AMOUNT_CACHE_NAMES)
public class AmountServiceImpl implements IAmountService{
    private static Logger logger = LoggerFactory.getLogger(AmountServiceImpl.class);

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

    @CachePut(key = "#amount.serialNo")
    @TargetDataSource
    @Override
    public Amount insertAmount(Amount amount) {
        logger.info("insertAmount, amount: {}, thread name: {}", amount.toString(), Thread.currentThread().getName());
        amountDao.insertAmount(amount);
        return amount;
    }

    @CachePut(key = "#amount.serialNo")
    @TargetDataSource
    @Override
    public Amount updateAmount(Amount amount) {
        logger.info("updateAmount, amount: {}, thread name: {}", amount.toString(), Thread.currentThread().getName());
        amountDao.updateAmount(amount);
        return amount;
    }
}
