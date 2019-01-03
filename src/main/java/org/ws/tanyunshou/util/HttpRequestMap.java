package org.ws.tanyunshou.util;

import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.TimeUnit;

/**
 * @author yinan
 * @date 19-1-2
 */
public class HttpRequestMap {

    private static BlockingMap<String, Amount> blockingMap = new BlockingHashMap<>();

    public static void put(String serialNo, Amount amount) throws InterruptedException {
        blockingMap.offer(serialNo, amount, 10, TimeUnit.SECONDS);
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
        return blockingMap.take(serialNo, 5, TimeUnit.SECONDS);
    }

    public static int size() {
        return blockingMap.size();
    }

}
