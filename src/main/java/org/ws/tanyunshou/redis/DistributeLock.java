package org.ws.tanyunshou.redis;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author yinan
 * @date 19-1-6
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DistributeLock {

    /**
     * 锁的资源 key
     * 支持el表达式
     * @return
     */
    @AliasFor("name")
    String name() default "'default'";

    /**
     * 锁的资源value
     * 支持el表达式
     * @return
     */
    @AliasFor("value")
    String value() default "'default'";

    /**
     * 持续时间
     */
    long keepMills() default 5000;

    /**
     * 获取锁失败时的动作
     * @return
     */
    LockFailAction action() default LockFailAction.CONTINUE;

    enum LockFailAction {
        /**
         * 放弃
         */
        GIVEUP,

        /**
         * 继续
         */
        CONTINUE
    }

    /**
     * 重试的时间间隔，设置GIVEUP忽略此项
     * @return
     */
    long sleepMills() default 200;

    /**
     * 重试次数
     * @return
     */
    int retryTimes() default 5;


}
