package com.akong.qqrobot.service;

import com.akong.qqrobot.vo.BotVo;

/**
 * 机器人管理接口
 *
 * @author Akong
 * @since 2022/2/6 6:46
 */
public interface IRobotService {
    /**
     * 机器人登录
     *
     * @param botVo 登录的机器人
     * @return 返回是否登录成功的布尔值
     */
    public boolean login(BotVo botVo);
}
