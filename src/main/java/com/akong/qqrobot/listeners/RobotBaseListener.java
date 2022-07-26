package com.akong.qqrobot.listeners;

import com.akong.qqrobot.config.GlobalData;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.component.mirai.message.event.MiraiBotInvitedJoinGroupRequest;
import love.forte.simbot.component.mirai.message.event.MiraiBotOffline;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 对机器人的基础监听
 *
 * @author Akong
 * @since 2022/2/6 7:09
 */
@Service
public class RobotBaseListener {

    /**
     * 监听机器人下线事件
     */
    @Listen(MiraiBotOffline.class)
    public void botOffline(MiraiBotOffline<BotOfflineEvent> botOffline) {
        // 修改此机器人的状态
        GlobalData.globalBots.get(botOffline.getAccountInfo().getAccountCode()).setOnline(false);
    }

    @Listen(MiraiBotInvitedJoinGroupRequest.class)
    public void inviteGroup(MiraiBotInvitedJoinGroupRequest groupRequest, MsgSender sender) {
        // 如果是我拉的，则同意
        if ("2835206867".equals(Objects.requireNonNull(groupRequest.getInvitor()).getInvitorCode())) {
            sender.SENDER.sendPrivateMsg(groupRequest.getInvitor().getInvitorCodeNumber(), "主人，我同意加入" + groupRequest.getGroupInfo().getGroupName() + "了喔 ฅ( ̳• ◡ • ̳)ฅ");
            groupRequest.getEvent().accept();
        }
    }
}
