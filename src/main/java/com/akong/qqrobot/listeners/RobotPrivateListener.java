package com.akong.qqrobot.listeners;

import com.akong.qqrobot.annotation.GroupFilter;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;

/**
 * @author Akong
 * @since 2022/2/6 12:12
 */
@Beans
public class RobotPrivateListener {
    @OnPrivate
    @GroupFilter
    public void privateMsg(PrivateMsg privateMsg, MsgSender sender) {
        if (!"2835206867".equals(privateMsg.getAccountInfo().getAccountCode()))
            sender.SENDER.sendPrivateMsg("2835206867", "主人主人，傻逼" + privateMsg.getAccountInfo().getAccountNicknameAndRemark() + "私聊对我说：" + privateMsg.getMsg());
    }
}
