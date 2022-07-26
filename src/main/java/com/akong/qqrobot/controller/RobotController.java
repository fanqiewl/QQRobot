package com.akong.qqrobot.controller;

import com.akong.qqrobot.annotation.GroupFilter;
import com.akong.qqrobot.config.GlobalData;
import com.akong.qqrobot.vo.BotVo;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.bot.BotVerifyException;
import love.forte.simbot.bot.BotVerifyInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 机器人的控制器
 *
 * @author Akong
 * @since 2022/2/6 8:09
 */
@RestController
@RequestMapping("/robot")
public class RobotController {
    @Resource
    private BotManager botManager;

    /**
     * 获取所有的机器人
     */
    @GroupFilter
    @RequestMapping("/list")
    public Map<String, BotVo> list() {
        return GlobalData.globalBots;
    }

    @RequestMapping("/login")
    public Bot login() {
        // 暂未成功，目测需要重构Robot工厂
        try {
            Bot bot = botManager.registerBot(BotVerifyInfo.withCodeVerification("3087609973", "99999"));
        } catch (BotVerifyException e) {
            System.err.println("=================================");
            e.printStackTrace();
            System.out.println("===" + e.getMessage());
        }
        return null;
    }
}
