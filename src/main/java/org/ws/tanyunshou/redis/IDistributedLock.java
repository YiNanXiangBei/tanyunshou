package org.ws.tanyunshou.redis;

/**
 * @author yinan
 * @date 19-1-6
 */
public interface IDistributedLock {

    long TIMEOUT_MILLS = 5000;

    int RETRY_TIMES = Integer.MAX_VALUE;

    long SLEEP_MILLS = 500;

    boolean lock(String key);

    boolean lock(String key, int retryTimes);

    boolean lock(String key, int retryTimes, long sleepMills);

    boolean lock(String key, long expire);

    boolean lock(String key, long expire, int retryTimes);

    boolean lock(String key, long expire, int retryTimes, long sleepMills);

    boolean releaseLock(String key);

}
