package org.ws.tanyunshou.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author yinan
 * @date 19-1-6
 */
@Aspect
@Configuration
@ConditionalOnClass(IDistributedLock.class)
@AutoConfigureAfter(DistributedLockAspect.class)
public class DistributedLockAspect {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    @Autowired
    private IDistributedLock distributedLock;

    private ExpressionParser parser = new SpelExpressionParser();

    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(org.ws.tanyunshou.redis.DistributeLock)")
    private void lockPoint() {

    }

    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        DistributeLock lockAction = method.getAnnotation(DistributeLock.class);
        String logKey = getLogKey(lockAction, point, method);
        int retryTimes = lockAction.action().equals(DistributeLock.LockFailAction.CONTINUE) ? lockAction.retryTimes() : 0;
        boolean isLock = distributedLock.lock(logKey, lockAction.keepMills(), retryTimes, lockAction.sleepMills());
        if (!isLock) {
            logger.debug("get lock failed : {}", logKey);
            return null;
        }

        //得到锁，执行方法释放锁
        logger.debug("get lock success : {}", logKey);
        try {
            return point.proceed();
        } catch (Exception e) {
            logger.error("execute locked method occured an exception {}", e);
        } finally {
            boolean releaseResult = distributedLock.releaseLock(logKey);
            logger.debug("release lock : {}", logKey + (releaseResult ? " success": " failed"));
        }

        return null;
    }

    /**
     * 获得分布式缓存的key
     * @param lockAction
     * @param point
     * @param method
     * @return
     */
    private String getLogKey(DistributeLock lockAction, ProceedingJoinPoint point, Method method) {
        String name = lockAction.name();
        String value = lockAction.value();
        Object[] args = point.getArgs();
        return parse(name, method, args) + "_" + parse(value, method, args);
    }

    /**
     * 解析EL表达式
     * @param key key
     * @param method method
     * @param args args
     * @return result
     */
    private String parse(String key, Method method, Object[] args) {
        String[] params = discoverer.getParameterNames(method);
        if (null == params || params.length == 0 || !key.contains("#")) {
            return key;
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

}
