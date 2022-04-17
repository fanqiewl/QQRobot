package com.akong.qqrobot.filters;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;

/**
 * 机器人过滤器
 *
 * @author Akong
 * @since 2022/2/6 9:53
 */
@Beans("MyBotFilter")
public class BotFilter implements ListenerFilter {

    @Override
    public boolean test(@NotNull FilterData data) {
        System.out.println(data);
        return true;
    }
}
