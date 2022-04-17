package com.akong.qqrobot.listeners;

import catcode.CatCodeUtil;
import catcode.CodeBuilder;
import com.akong.qqrobot.config.GlobalData;
import com.akong.qqrobot.model.Music;
import com.akong.qqrobot.util.MusicUtil;
import com.akong.qqrobot.util.SaveLogUtil;
import com.fasterxml.jackson.databind.JsonNode;
import kotlinx.coroutines.TimeoutCancellationException;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.utils.Carrier;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.assists.Flag;
import love.forte.simbot.api.message.events.GroupMemberPermissionChanged;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import net.mamoe.mirai.message.data.MusicKind;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 机器人群聊监听器
 *
 * @author Akong
 * @since 2022/2/6 8:38
 */
@Beans
public class RobotGroupListener {
    private final SaveLogUtil logUtil = new SaveLogUtil();
    // 猫猫码工具类
    private final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;

    @OnGroup
    @Filters(value = {@Filter(atBot = true, value = "音乐", trim = true)})
    public void randomMusic(GroupMsg groupMsg, MsgSender sender) {
        logUtil.groupLog(groupMsg, "=音乐=");
        musicToDay(groupMsg, sender);
    }

    @OnGroup
    @Filter(atBot = true, value = "点歌", matchType = MatchType.STARTS_WITH, trim = true)
    public void chooseSong(GroupMsg groupMsg, ListenerContext context, MsgSender sender) {
        // 截取歌曲关键字
        String str = groupMsg.getText().substring(groupMsg.getText().indexOf("点歌") + 2).trim();
        // 判断长度
        if (str.length() >= 20) {
            // 关键字过长进行拦截
            sender.SENDER.sendGroupMsg(groupMsg, "干点阳间的事叭，求求了，哪有音乐这么长的");
            return;
        }

        // 得到群号
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        // 得到请求人账号
        String accountCode = groupMsg.getAccountInfo().getAccountCode();

        // 拼接得出唯一key
        String key = groupCode + ":" + accountCode;

        // 得到session上下文
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        // 断言session的确不为null
        assert session != null;

        // 判定是否已在点歌中
        if (session.get(GlobalData.MUSIC_GROUP, key) != null) {
            // 该人正在点歌 ==》 发送提示，拦截请求
            sender.SENDER.sendGroupMsg(groupMsg, "吊毛" + groupMsg.getAccountInfo().getAccountRemarkOrNickname() + "，您正在点歌，不要搞这些阴间操作");
            return;
        }

        try {
            // 调用工具类获取歌曲
            GlobalData.musicMap.put(key, MusicUtil.chooseSong(str, 10));
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg, "被玩坏了，绝对不是" + groupMsg.getBotInfo().getAccountRemarkOrNickname() + "的错 Σ( ° △ °|||)");
            e.printStackTrace();
            throw new RuntimeException("点歌搜索音乐时出错");
        }

        // 获取音乐列表
        List<Music> musicList = GlobalData.musicMap.get(key);
        // 定义提示字符串
        String prompt = "共搜索到" + musicList.size() + "首音乐\n";
        // 发送结果提示
        for (int i = 0; i < musicList.size(); i++) {
            // 获取当前音乐
            Music music = musicList.get(i);
            // 获取音乐名
            String musicName = music.getName();
            // 获取歌手
            String artist = music.getArtist();
            // 拼接字符串
            prompt += (i + 1) + "、" + musicName + " - " + artist + "\n";
        }
        // 拼接选择提示
        prompt += "\n请" + groupMsg.getAccountInfo().getAccountRemarkOrNickname() + "在10s内使用序号选择音乐";
        // 发送提示
        sender.SENDER.sendGroupMsg(groupMsg, prompt);

        // 构建回调函数
        final SessionCallback<Integer> callback = SessionCallback.<Integer>builder().onResume(choose -> {
            choose -= 1;
            // 判断下标越界问题
            if (GlobalData.musicMap.get(key).size() < choose) {
                sender.SENDER.sendGroupMsg(groupMsg, "那啥，咱能不能把序号选对，数学是体育老师教的嘛，喵喵~");
                session.remove(GlobalData.MUSIC_GROUP, key);
                return;
            }

            // 获取点播的音乐
            Music music = GlobalData.musicMap.get(key).get(choose);

            // 定义歌曲信息
            String musicName = music.getName() + " - " + music.getArtist();

            // 信息列表显示文字
            String brief = new String(("来自" + groupMsg.getAccountInfo().getAccountRemarkOrNickname() + "的点播：" + musicName).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            // 卡片内容信息
            String summary = new String(musicName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            // 音乐链接
            String musicUrl;
            try {
                musicUrl = MusicUtil.extractLink(music);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("音乐链接提取失败！！！");
            }
            // 默认音乐图片
            String defaultPicture = music.getPic() == null ? "https://music.akongwl.top/static/imgs/icon.png" : music.getPic();

            sender.SENDER.sendGroupMsg(groupMsg, "[" + ("CAT:music,title=AkongMusic,type=" + MusicKind.NeteaseCloudMusic + ",picture=" + defaultPicture + ",jump=https://music.akongwl.top/,brief=" + brief + ",summary=" + summary + ",musicUrl=" + musicUrl).replaceAll("]", ")").replaceAll("\\[", "(") + "]");

        }).onError(e -> {
            e.printStackTrace();
            // 移除会话中的点歌
            session.remove(GlobalData.MUSIC_GROUP, key);
            if (e instanceof TimeoutCancellationException) {
                sender.SENDER.sendGroupMsg(groupMsg, groupMsg.getAccountInfo().getAccountRemarkOrNickname() + "的点歌超时啦！！！");
            } else
                sender.SENDER.sendGroupMsg(groupMsg, "被玩坏了，绝对不是" + groupMsg.getBotInfo().getAccountRemarkOrNickname() + "的错 Σ( ° △ °|||)");
        }).build();

        session.waiting(GlobalData.MUSIC_GROUP, key, 10 * 1000, callback);
    }

    @OnGroup
    @Filters(value = {@Filter(atBot = true, value = "二次元", trim = true)})
    public void randomPicture(Bot bot, GroupMsg groupMsg, MsgSender sender) {
        logUtil.groupLog(groupMsg, "==二次元图片==");

        // 构建猫猫码
        CodeBuilder<String> catImage = catUtil.getStringCodeBuilder("image", false);
        String image = catImage.key("file").value("https://api.mtyqx.cn/tapi/random.php").build();

        sender.SENDER.sendGroupMsg(groupMsg, image);
    }

    @Listen(GroupMemberPermissionChanged.class)
    public void changeMemberPermission(GroupMemberPermissionChanged permissionChanged, MsgSender sender) {
        if (permissionChanged.isGetManagementRights()) // 得到管理员权限
            sender.SENDER.sendGroupMsg(permissionChanged.getGroupInfo(), "恭喜" + permissionChanged.getAccountInfo().getAccountRemarkOrNickname() + "成为本群管理员，喵喵喵 o‿≖✧");
        else // 取消管理员权限
            sender.SENDER.sendGroupMsg(permissionChanged.getGroupInfo(), "可怜的" + permissionChanged.getAccountInfo().getAccountRemarkOrNickname() + "沦为了平民，本喵为你默哀 ε(╥﹏╥)3");
    }

    private void musicToDay(GroupMsg groupMsg, MsgSender sender) {
        // 调用帮助类获取到每日推荐音乐
        JsonNode jsonNode = null;

        try {
            jsonNode = MusicUtil.toDayMusic();
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupMsg, "绝对不是" + groupMsg.getBotInfo().getAccountNickname() + "的错，请再试一遍叭，喵呜(๑•̀ㅁ•́ฅ)");
            throw new RuntimeException("歌曲获取失败");
        }

        // 定义歌曲信息
        String musicName = jsonNode.get("name").asText() + " - " + jsonNode.get("artist").asText();

        // 信息列表显示文字
        String brief = new String(("今日推荐：" + musicName).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        // 卡片内容信息
        String summary = new String(musicName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        // 音乐链接
        String musicUrl = jsonNode.get("musicUrl").asText();

        // MsgSender中存在三大送信器，以及非常多的重载方法。
        sender.SENDER.sendGroupMsg(groupMsg, "[CAT:music,title=AkongMusic,type=" + MusicKind.NeteaseCloudMusic + ",jump=https://music.akongwl.top/,brief=" + brief + ",summary=" + summary + ",musicUrl=" + musicUrl + "]");
    }

    @OnGroup
    @OnlySession(group = GlobalData.MUSIC_GROUP)
    @Filter(value = "([1-9]|10)", matchType = MatchType.REGEX_MATCHES)
    public void checkMusic(GroupMsg groupMsg, ListenerContext context) {
        // 得到session上下文
        ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        // 断言session的确不为空
        assert session != null;

        // 得到群号
        String groupCode = groupMsg.getGroupInfo().getGroupCode();
        // 得到请求人账号
        String accountCode = groupMsg.getAccountInfo().getAccountCode();

        // 拼接得出唯一key
        String key = groupCode + ":" + accountCode;
        System.out.println(key);
        System.out.println(session.getKeys());

        // 得到消息内容（转为数字 == 即为音乐序号）
        Integer num = Integer.parseInt(groupMsg.getText());

        // 将得到的音乐序号push给对应的会话
        session.push(GlobalData.MUSIC_GROUP, key, num);
    }
}
