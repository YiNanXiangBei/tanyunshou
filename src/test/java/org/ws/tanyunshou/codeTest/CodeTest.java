package org.ws.tanyunshou.codeTest;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinan
 * @date created in 下午1:49 18-12-29
 */

public class CodeTest {


    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 12, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 300; i++) {
            executor.execute(new TestTask());
        }
    }

    static class TestTask implements Runnable {

        @Override
        public void run() {
            for (;;) {
                try {
                    test();
                    break;
                } catch (Exception e) {
                    System.out.println("执行一些异常操作");
                }
//                break;
            }
            System.out.println("所有操作结束");
        }

        private void test() throws Exception {
            synchronized (this) {
                Random random = new Random();
                int val = random.nextInt(10);
                if (val != 2) {
                    throw new Exception();
                } else {
                    System.out.println("执行一些操作");
                }
            }
        }
    }

}
