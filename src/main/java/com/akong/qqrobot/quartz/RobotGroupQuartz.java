package com.akong.qqrobot.quartz;

import com.akong.qqrobot.apiclient.TianApiClient;
import com.akong.qqrobot.util.MusicUtil;
import com.fasterxml.jackson.databind.JsonNode;
import love.forte.simbot.api.message.results.SimpleGroupInfo;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotManager;
import net.mamoe.mirai.message.data.MusicKind;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 定时任务
 *
 * @author Akong
 * @since 2022/2/6 12:01
 */
@Component
@EnableScheduling
public class RobotGroupQuartz {
    @Resource
    private BotManager botManager;
    @Resource
    private TianApiClient tianApiClient;

    /**
     * 早晨问候
     */
    @Scheduled(cron = "0 0 8 * * ? ")
    public void mornGreeting() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> {
            bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "大佬们起床啦，今天也要开心喵 ฅ•ω•ฅ");
            bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "[今日名句] " + tianApiClient.ancientBooks());
            musicToDay(simpleGroupInfo, bot);
        });
    }

    /**
     * 早餐提醒
     */
    @Scheduled(cron = "0 0 9 * * ? ")
    public void breakFast() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "喵！大佬们吃早饭了吗？"));
    }

    /**
     * 午间问候
     */
    @Scheduled(cron = "0 0 12 * * ? ")
    public void middyGreeting() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "大佬们别卷啦，该吃午饭咯，喵喵喵 ฅ( ＞ω＜)ฅ"));
    }

    /**
     * 午休提醒
     */
    @Scheduled(cron = "0 0 13 * * ? ")
    public void lunchBreak() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "一点了，大佬们睡个午觉休息一下喵 (=｀ω´=)"));
    }

    /**
     * 下午上班提醒
     */
    @Scheduled(cron = "0 0 14 * * ? ")
    public void afternoonWorkTime() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "哎呀，又该摸鱼了喵 V(=^･ω･^=)v"));
    }

    /**
     * 晚饭提醒
     */
    @Scheduled(cron = "0 30 17 * * ? ")
    public void dinnerGreeting() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, "大佬们要考虑去吃晚饭嘛，想吃冰淇淋喵（=´∇｀=）"));
    }

    /**
     * 晚安心语
     */
    @Scheduled(cron = "0 0 22 * * ? ")
    public void nightGreeting() {
        Bot bot = botManager.getBots().get(0);
        bot.getSender().GETTER.getGroupList().forEach(simpleGroupInfo -> bot.getSender().SENDER.sendGroupMsg(simpleGroupInfo, tianApiClient.goodNight()));
    }


    private void musicToDay(SimpleGroupInfo groupInfo, Bot bot) {
        // 调用帮助类获取到每日推荐音乐
        JsonNode jsonNode;
        BotSender sender = bot.getSender();

        try {
            jsonNode = MusicUtil.toDayMusic();
        } catch (Exception e) {
            sender.SENDER.sendGroupMsg(groupInfo, "绝对不是" + bot.getBotInfo().getAccountNickname() + "的错，请再试一遍叭，喵呜(๑•̀ㅁ•́ฅ)");
            throw new RuntimeException("歌曲获取失败");
        }

        // 定义歌曲信息
        String musicName = jsonNode.get("name").asText() + " - " + jsonNode.get("artist").asText();

        // 卡片内容信息
        String summary = new String(musicName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        // 音乐链接
        String musicUrl = jsonNode.get("musicUrl").asText();

        // 信息列表显示文字
        String brief = new String(("今日推荐：" + musicName).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        // MsgSender中存在三大送信器，以及非常多的重载方法。
        sender.SENDER.sendGroupMsg(groupInfo, "[CAT:music,title=AkongMusic,type=" + MusicKind.NeteaseCloudMusic + ",jump=https://music.akongwl.top/,brief=" + brief + ",summary=" + summary + ",musicUrl=" + musicUrl + "]");
    }
}
