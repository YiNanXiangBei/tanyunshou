package org.ws.tanyunshou.codeTest;

import org.junit.Test;
import org.ws.tanyunshou.vo.Amount;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date created in 下午1:49 18-12-29
 */

public class CodeTest {
    public static void main(String[] args) {
        Amount amount1 = new Amount("1", new BigDecimal(1), "1");
        Amount amount2 = new Amount("1", new BigDecimal(1), "1");
        String str = new String("1");
        String str1 = new String("1");
        System.out.println(amount1.hashCode());
        System.out.println(amount2.hashCode());
    }
}
