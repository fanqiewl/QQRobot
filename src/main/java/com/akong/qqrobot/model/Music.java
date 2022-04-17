package com.akong.qqrobot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 音乐实体类
 *
 * @author Akong
 * @since 2022/2/16 19:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class Music {
    // 音乐编号
    private String rid;
    // 音乐图片路径
    private String pic;
    // 音乐名
    private String name;
    // 歌手
    private String artist;
    // 专辑
    private String album;
    // 音乐时长
    private String songTimeMinutes;
    // 音质
    private String[] coopFormats;
    // 歌曲链接
    private String musicUrl;
    // 判断是否为存储于数据库内的音乐
    private boolean localMusic = true;
}
