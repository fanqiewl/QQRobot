package com.akong.qqrobot.util;

import com.akong.qqrobot.model.Music;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐请求帮助类
 *
 * @author Akong
 * @since 2022/2/4 17:23
 */
public class MusicUtil {
    // 实体JSON转换工具
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 每日推荐音乐
     *
     * @return 返回节点对象
     */
    public static JsonNode toDayMusic() throws Exception {
        // 设置请求链接
        URL url = new URL("https://music.api.akongwl.top/api/toDayMusic");
        // 开启连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 调用帮助工具得到数据（并转为实体）
        return objectMapper.readTree(HttpConUtil.requestCon(con).toString());
    }


    /**
     * 提取音乐链接
     *
     * @param music 音乐实体
     * @return 返回音乐链接
     */
    public static String extractLink(Music music) throws Exception {
        // 定义默认提取链接
        String path = "https://music.api.akongwl.top/api/exMusicLink";
        // 定义参数
        String params = "?rid=" + music.getRid() + "&format=" + music.getCoopFormats()[music.getCoopFormats().length - 1];

        // 判断若为永硕引擎
        if (music.isLocalMusic()) {
            // 修改提取链接
            path = "https://music.api.akongwl.top/api/exYs168MusicLink";
            // 修改参数
            params = "?ysid=" + music.getRid() + "-" + music.getCoopFormats()[0];
        }

        // 设置请求链接
        URL url = new URL(path + params);
        // 开启连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        return objectMapper.readTree(HttpConUtil.requestCon(con).toString()).asText();
    }

    /**
     * 点歌搜索
     *
     * @return 返回节点对象
     */
    public static List<Music> chooseSong(String musicName, Integer rows) throws Exception {
        // 定义音乐集合
        List<Music> musicList;
        // 定义临时集合
        List<Music> tempMusic;

        // 优先永硕盘搜索
        tempMusic = selMusic(musicName, rows, null, 1);
        // 判断得到的音乐数量（若满足一次需要的数量则直接返回）
        if (tempMusic.size() >= 10)
            return tempMusic;
        else {
            // 否则将总数与得到的数量进行相减
            rows -= tempMusic.size();
            // 将得到的音乐添加到集合中
            musicList = new ArrayList<>(tempMusic);
        }

        // 继续使用默认引擎搜索
        tempMusic = selMusic(musicName, rows, null, null);
        // 遍历修改状态
        tempMusic.forEach(music -> music.setLocalMusic(false));
        // 将得到的音乐添加到集合中
        musicList.addAll(tempMusic);

        // 返回最终音乐集合
        return musicList;
    }

    /**
     * 搜索音乐
     *
     * @return 返回音乐集合
     */
    public static ArrayList<Music> selMusic(String musicName, Integer rows, Integer page, Integer engine) throws Exception {
        // 定义请求链接（默认引擎）
        String path = "https://music.api.akongwl.top/api/selMusic";
        // 永硕引擎
        if (null != engine && engine == 1)
            path = "https://music.api.akongwl.top/api/selMusic_ys168";

        // 参数：搜索关键字
        path += "?musicName=" + (StringUtils.isNotBlank(musicName) ? URLEncoder.encode(musicName, "UTF-8") : "");
        // 参数：显示条数
        path += "&rows=" + (null == rows ? 10 : rows);
        // 参数：当前页码
        path += "&page=" + (null == page ? 1 : page);

        // 设置请求链接
        URL url = new URL(path);

        // 开启连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // 调用帮助工具得到数据（并转为实体）
        JsonNode jsonNode = objectMapper.readTree(HttpConUtil.requestCon(con).toString());
        // 提取音乐集合（并转为音乐集合）
        return objectMapper.readValue(null != engine ? jsonNode.traverse() : jsonNode.get("musicList").traverse(), new TypeReference<ArrayList<Music>>() {
        });
    }
}
