package com.akong.qqrobot.config;

import com.akong.qqrobot.model.Music;
import com.akong.qqrobot.vo.BotVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局数据
 *
 * @author Akong
 * @since 2022/2/6 7:11
 */
public class GlobalData {
    /*所有的机器人集合*/
    public static Map<String, BotVo> globalBots = new HashMap<>();
    /*点歌功能：音乐集合*/
    public static Map<String, List<Music>> musicMap = new HashMap<>();
    /*音乐分组*/
    public static final String MUSIC_GROUP = "music_group";
}
