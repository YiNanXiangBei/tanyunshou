package org.ws.tanyunshou.util;

import org.thavam.util.concurrent.blockingMap.BlockingHashMap;
import org.thavam.util.concurrent.blockingMap.BlockingMap;
import org.ws.tanyunshou.vo.Amount;

/**
 * @author yinan
 * @date 19-1-2
 */
public class HttpRequestMap {

    private static BlockingMap<String, Amount> blockingMap = new BlockingHashMap<>();

    public static void put(String serialNo, Amount amount) {
        blockingMap.put(serialNo, amount);
    }

    public static Amount take(String serialNo) throws InterruptedException {
        return blockingMap.take(serialNo);
    }

}
