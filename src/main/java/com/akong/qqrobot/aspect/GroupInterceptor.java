package com.akong.qqrobot.aspect;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.intercept.InterceptionType;
import love.forte.simbot.listener.ListenerInterceptContext;
import love.forte.simbot.listener.ListenerInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 群聊管制
 *
 * @author Akong
 * @since 2022/2/8 18:42
 */
@Slf4j
@Component
public class GroupInterceptor implements ListenerInterceptor {
    @NotNull
    @Override
    public InterceptionType intercept(@NotNull ListenerInterceptContext context) {
//        System.out.println(context.getListenerFunction().getAnnotation(GroupFilter.class));
        return InterceptionType.PASS;
    }
}
