package com.akong.qqrobot;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.core.SimbotApp;

/**
 * QQ机器人启动器
 *
 * @author Akong
 * @since 2022/2/6 4:17
 */
@SimbotApplication
public class MyRobotApplication {
    public static void main(String[] args) {
        SimbotApp.run(MyRobotApplication.class, args);
    }
}