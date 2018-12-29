package org.ws.tanyunshou.codeTest;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date created in 下午1:49 18-12-29
 */

public class CodeTest {


    @Test
    public void test() {
        BigDecimal decimal = new BigDecimal(10);
        decimal = decimal.add(new BigDecimal(-1));
        System.out.println(decimal.toString());
    }

}
