package org.ws.tanyunshou.dao;

import org.springframework.stereotype.Repository;
import org.ws.tanyunshou.vo.Amount;

import java.util.List;

/**
 * @author yinan
 * @date created in 上午11:06 18-12-26
 */
public class AmountDaoImpl implements IAmountDao {
    @Override
    public Amount findAmountBySerialNo(String serialNo) {
        return null;
    }

    @Override
    public List<Amount> findAllAmounts() {
        return null;
    }

    @Override
    public int insertAmount(Amount amount) {
        return 0;
    }

    @Override
    public int updateAmount(Amount amount) {
        return 0;
    }
}
