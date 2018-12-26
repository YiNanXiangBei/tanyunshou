package org.ws.tanyunshou.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.ws.tanyunshou.vo.Amount;
import java.util.List;

/**
 * @author yinan
 * @date created in 上午11:06 18-12-26
 */
@Mapper
public interface IAmountDao {

    /**
     * 通过序列号查找金额信息
     * @param serialNo
     * @return Amount
     */
    Amount findAmountBySerialNo(String serialNo);

    /**
     * 查询数据库中所有金额信息
     * @return
     */
    List<Amount> findAllAmounts();

    /**
     * 插入新的金额信息
     * @param amount
     * @return 成功则返回成功条数
     */
    int insertAmount(Amount amount);

    /**
     * 更新金额信息
     * @param amount
     * return 成功返回位置
     */
    int updateAmount(Amount amount);

}
