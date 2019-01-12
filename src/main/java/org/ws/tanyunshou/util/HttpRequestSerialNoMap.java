package org.ws.tanyunshou.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yinan
 * @date created in 下午2:34 19-1-12
 */
@Component
public class HttpRequestSerialNoMap {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestSerialNoMap.class);

    private ConcurrentHashMap<String, Amount> hashMap = new ConcurrentHashMap<>();

    public void put(String key, Amount value) {
        logger.info("put result to map, key is {}, value is {}", key, value);
        hashMap.put(key, value);
    }

    public Amount take(String key) {
        Amount amount = hashMap.get(key);
        logger.info("get result, key is {}, value is {}", key, amount);
        hashMap.remove(key);
        return amount;
    }

    public int size() {
        return hashMap.size();
    }

}
