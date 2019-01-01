package org.ws.tanyunshou.codeTest;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author yinan
 * @date created in 下午1:49 18-12-29
 */

public class CodeTest {



    public static void main(String[] args) {
        synchronized (CodeTest.class) {
            System.out.println("Synchronize");
        }
    }

}
