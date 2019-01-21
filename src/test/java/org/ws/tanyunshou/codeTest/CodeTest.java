package org.ws.tanyunshou.codeTest;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yinan
 * @date created in 下午1:49 18-12-29
 */

public class CodeTest {

    private static CountDownLatch latch = new CountDownLatch(1);


    public static void main(String[] args) {
        Student student = new Student();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 12, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 300; i++) {
            executor.execute(new TestTask(student));
        }
    }

    static class TestTask implements Runnable {

        public TestTask(Student student) {
            this.student = student;
        }

        private Student student;

        @Override
        public void run() {
            for (;;) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                student = new Student("zhangsan");
                break;
            }
            System.out.println(student.getName());
        }
    }

}
