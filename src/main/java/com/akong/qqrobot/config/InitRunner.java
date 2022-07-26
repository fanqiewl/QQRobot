package com.akong.qqrobot.config;

import com.akong.qqrobot.util.MyUtils;
import love.forte.simbot.bot.BotManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 初始化配置
 *
 * @author Akong
 * @since 2022/2/6 7:37
 */
@Order
@Component
public class InitRunner implements ApplicationRunner {
    @Resource
    private BotManager botManager;

    @Override
    public void run(ApplicationArguments args) {
        // 装配上线的机器人
        botManager.getBots().forEach(bot -> GlobalData.globalBots.put(bot.getBotInfo().getAccountCode(), MyUtils.BotInfoParseBotVo(bot.getBotInfo())));
    }
}
