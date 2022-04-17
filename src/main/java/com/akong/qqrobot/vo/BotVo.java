package com.akong.qqrobot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 显示所用的机器人对象
 *
 * @author Akong
 * @since 2022/2/6 7:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class BotVo implements Serializable {
    // 账号
    private String code;
    // 昵称
    private String nickName;
    // 在线状态
    private boolean online;
}
