package com.akong.qqrobot.util;

import com.akong.qqrobot.vo.BotVo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.bot.Bot;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类（存放一些小工具）
 *
 * @author Akong
 * @since 2022/2/6 7:06
 */
public class MyUtils {
    /**
     * 将BotInfo转为BotVo
     *
     * @return 返回便于使用的BotVo对象
     */
    public static BotVo BotInfoParseBotVo(BotInfo botInfo) {
        return new BotVo()
                .setCode(botInfo.getAccountCode())
                .setNickName(botInfo.getAccountNickname())
                .setOnline(true);
    }

    /**
     * 将Bot集合转为BotVo集合
     *
     * @param bots 需要Bot集合
     * @return 返回便于使用的BotVo集合
     */
    public static List<BotVo> BotsParseBotVos(List<Bot> bots) {
        if (bots == null)
            throw new RuntimeException("传入的Bot集合为空");

        List<BotVo> botVos = new ArrayList<>();
        bots.forEach(bot -> botVos.add(BotInfoParseBotVo(bot.getBotInfo())));

        return botVos;
    }
}
