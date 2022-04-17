package com.akong.qqrobot.config;

import love.forte.simbot.bot.BotVerifyInfo;
import love.forte.simbot.component.mirai.MiraiBotConfigurationFactory;
import love.forte.simbot.component.mirai.configuration.MiraiConfiguration;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * @author Akong
 * @since 2022/2/9 11:14
 */
public class MyBotConfigurationFactory implements MiraiBotConfigurationFactory {
    @NotNull
    @Override
    public BotConfiguration getMiraiBotConfiguration(@NotNull BotVerifyInfo botInfo, @NotNull MiraiConfiguration simbotMiraiConfig) {
        return simbotMiraiConfig.getBotConfiguration().invoke("");
    }
}
