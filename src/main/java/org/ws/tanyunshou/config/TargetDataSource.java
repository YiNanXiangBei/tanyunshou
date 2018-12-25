package org.ws.tanyunshou.config;

import java.lang.annotation.*;

/**
 * @author yinan
 * @date created in 下午4:08 18-12-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name() default "";
}
