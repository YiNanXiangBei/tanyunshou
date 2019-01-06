package org.ws.tanyunshou.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author yinan
 * @date created in 下午4:12 18-12-25
 */
@Aspect
@Component
@Order(0)
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(org.ws.tanyunshou.config.TargetDataSource)")
    public void dataSourcePoint() {

    }

    @Before("dataSourcePoint()")
    public void around(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TargetDataSource dataSource = method.getAnnotation(TargetDataSource.class);
        if (StringUtils.isEmpty(dataSource)) {
            DynamicDataSource.setDataSource(DataSourceNames.MASTER);
            logger.debug("set datasource is " + DataSourceNames.MASTER);
        } else {
            DynamicDataSource.setDataSource(dataSource.name());
            logger.debug("set datasource is " + dataSource.name());
        }
    }

    @AfterReturning("dataSourcePoint()")
    public void after() {
        DynamicDataSource.clearDataSource();
        logger.debug("clean datasource");
    }


}
