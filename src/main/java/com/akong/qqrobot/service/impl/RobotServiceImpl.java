package com.akong.qqrobot.service.impl;

import com.akong.qqrobot.config.GlobalData;
import com.akong.qqrobot.service.IRobotService;
import com.akong.qqrobot.vo.BotVo;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.mirai.message.event.MiraiBotInvitedJoinRequestFlagContent;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Akong
 * @since 2022/2/6 8:23
 */
@Service
public class RobotServiceImpl implements IRobotService {
    @Resource
    private BotManager botManager;

    @Override
    public boolean login(BotVo botVo) {
        // 首先在机器人列表中找到该机器人（待续。。。）
        GlobalData.globalBots.get(botVo.getCode());
        return false;
    }
}
