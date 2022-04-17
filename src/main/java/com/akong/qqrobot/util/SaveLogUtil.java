package com.akong.qqrobot.util;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author Akong
 * @since 2022/2/6 11:25
 */
@Slf4j
@Component
public class SaveLogUtil {

    public void groupLog(@NotNull GroupMsg groupMsg,@NotNull String name){
        // 获取群名称【群号】
        String group = groupMsg.getGroupInfo().getGroupName()+"【"+groupMsg.getGroupInfo().getGroupCode()+"】";
        // 获取操作人【账号】
        String account = groupMsg.getAccountInfo().getAccountNickname()+"["+groupMsg.getAccountInfo().getAccountCode()+"]";
        // 打印日志
        log.info(group+"群的"+account+"使用了"+name+"功能");
    }

    public void privateLog(PrivateMsg privateMsg){

    }
}
