package com.akong.qqrobot.annotation;

import java.lang.annotation.*;

/**
 * 异常重试机制注解
 *
 * @author Akong
 * @since 2022/7/25 16:23
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionRetry {

    /**
     * @return 重试次数/次
     */
    int retryCount() default 3;

    /**
     * @return 重试间隔时间/s
     */
    int waitTimes() default 1;

    /**
     * @return 不重试直接抛出的异常
     */
    Class[] throwExceptions() default {};
}