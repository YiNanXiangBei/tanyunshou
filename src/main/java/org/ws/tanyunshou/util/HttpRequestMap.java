package org.ws.tanyunshou.util;

import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yinan
 * @date 19-1-2
 */
public class HttpRequestMap {

    private static ConcurrentHashMap<String, Amount> hashMap = new ConcurrentHashMap<>();

    private static final Lock LOCK = new ReentrantLock();

    private static final Condition PUT_CONDITION = LOCK.newCondition();

    private static final Condition TAKE_CONDITION = LOCK.newCondition();

    private static final int MAX_SIZE = 10000;

    public static void put(String serialNo, Amount amount) throws InterruptedException {
        try {
            LOCK.lock();
            while (hashMap.size() == MAX_SIZE) {
                PUT_CONDITION.await();
            }
            hashMap.put(serialNo, amount);
            TAKE_CONDITION.signal();
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 15
     *
     * 秒没有返回直接返回空
     * @param serialNo
     * @return
     * @throws InterruptedException
     */
    public static Amount take(String serialNo) throws InterruptedException {
        try {
            LOCK.lock();
            while (hashMap.isEmpty() || !hashMap.containsKey(serialNo)) {
                TAKE_CONDITION.await();
            }
            Amount amount = hashMap.get(serialNo);
            PUT_CONDITION.signal();
            return amount;
        } finally {
            LOCK.unlock();
        }
    }

    public static int size() {
        return hashMap.size();
    }

}
