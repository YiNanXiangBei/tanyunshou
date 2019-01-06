package org.ws.tanyunshou.redis;

/**
 * @author yinan
 * @date 19-1-6
 */
public class AbstractDistributeLockImpl implements IDistributedLock {

    @Override
    public boolean lock(String key) {
        return lock(key, TIMEOUT_MILLS, RETRY_TIMES, SLEEP_MILLS);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return lock(key, TIMEOUT_MILLS, retryTimes, SLEEP_MILLS);
    }

    @Override
    public boolean lock(String key, int retryTimes, long sleepMills) {
        return lock(key, TIMEOUT_MILLS, retryTimes, sleepMills);
    }

    @Override
    public boolean lock(String key, long expire) {
        return lock(key, expire, RETRY_TIMES, SLEEP_MILLS);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes) {
        return lock(key, expire, retryTimes, SLEEP_MILLS);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMills) {
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        return false;
    }
}
