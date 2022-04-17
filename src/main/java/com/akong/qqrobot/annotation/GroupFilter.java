package com.akong.qqrobot.annotation;

import java.lang.annotation.*;

/**
 * 群聊过滤注解
 *
 * @author Akong
 * @since 2022/2/8 18:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GroupFilter {
}
