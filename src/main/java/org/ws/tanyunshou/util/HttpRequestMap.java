package org.ws.tanyunshou.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.ws.tanyunshou.message.ResponseMessage;
import org.ws.tanyunshou.vo.Amount;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yinan
 * @date 19-1-2
 */
@Component
public class HttpRequestMap {

    private static ConcurrentHashMap<String, DeferredResult<ResponseMessage>> hashMap = new ConcurrentHashMap<>();

    private static Logger logger = LoggerFactory.getLogger(HttpRequestMap.class);

    public  void put(String key, DeferredResult<ResponseMessage> result) {
        logger.info("put result to map, key is {}, value is {}", key, result);
        hashMap.put(key, result);
    }

    /**
     * 15
     *
     * 秒没有返回直接返回空
     * @param key
     * @return
     */
    public DeferredResult<ResponseMessage> take(String key){
        DeferredResult<ResponseMessage> result = hashMap.get(key);
        logger.info("get result, key is {}, value is {}", key, result);
        hashMap.remove(key);
        return result;
    }

    public static int size() {
        return hashMap.size();
    }

}
