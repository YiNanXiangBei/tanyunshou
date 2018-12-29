package org.ws.tanyunshou.service;

import org.ws.tanyunshou.vo.Amount;
import java.util.List;

/**
 * @author yinan
 * @date created in 上午11:08 18-12-26
 */

public interface IAmountService {
    Amount findAmountBySerialNo(String serialNo);

    List<Amount> findAllAmounts();

    Amount insertAmount(Amount amount);

    Amount  updateAmount(Amount amount);

}
