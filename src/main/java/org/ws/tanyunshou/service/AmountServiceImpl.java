package org.ws.tanyunshou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ws.tanyunshou.config.DataSourceNames;
import org.ws.tanyunshou.config.TargetDataSource;
import org.ws.tanyunshou.dao.IAmountDao;
import org.ws.tanyunshou.vo.Amount;

import java.util.List;

/**
 * @author yinan
 * @date created in 上午11:08 18-12-26
 */
@Service
public class AmountServiceImpl implements IAmountService{

    @Autowired
    private IAmountDao amountDao;

    @TargetDataSource(name = DataSourceNames.SLAVE)
    @Override
    public Amount findAmountBySerialNo(String serialNo) {
        return amountDao.findAmountBySerialNo(serialNo);
    }

    @TargetDataSource(name = DataSourceNames.SLAVE)
    @Override
    public List<Amount> findAllAmounts() {
        return amountDao.findAllAmounts();
    }

    @TargetDataSource
    @Override
    public void insertAmount(Amount amount) {
        amountDao.insertAmount(amount);
    }

    @TargetDataSource
    @Override
    public void updateAmount(Amount amount) {
        amountDao.updateAmount(amount);
    }
}
