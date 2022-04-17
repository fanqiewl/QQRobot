package com.akong.qqrobot.apiclient;

import com.akong.qqrobot.util.HttpConUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 天行数据api操作
 *
 * @author Akong
 * @since 2022/2/7 6:23
 */
@Component
public class TianApiClient {
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 晚安心语
     *
     * @return 返回JSON字符串
     */
    public String goodNight() {
        try {
            // 将JSON数据转为实体对象
            JsonNode jsonNode = objectMapper.readTree(apiClient("https://api.tianapi.com/wanan/index"));
            // 是否成功判断
            if (200 != jsonNode.get("code").asInt())
                // 返回默认的信息
                return "用最孤独的时光塑造出最好的自己，才能笑着对别人说那些云淡风轻的过去。晚安！";

            // 返回得到的信息
            return jsonNode.get("news" + "list").get(0).get("content").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("获取晚安心语发生异常");
        }
    }

    /**
     * 古籍名句
     *
     * @return 名句字符串
     */
    public String ancientBooks() {
        try {
            // 将JSON数据转为实体对象
            JsonNode jsonNode = objectMapper.readTree(apiClient("https://api.tianapi.com/gjmj/index"));
            // 是否成功判断
            if (200 != jsonNode.get("code").asInt())
                // 返回默认的信息
                return "知我者，谓我心忧；不知我者，谓我何求\r\n\t\t\t——诗经·王风";

            // 返回得到的信息
            return jsonNode.get("news" + "list").get(0).get("content").asText() + "\r\n\t\t\t——" + jsonNode.get("news" + "list").get(0).get("source").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("获取古籍名句发生异常");
        }
    }

    /**
     * 封装请求连接代码
     *
     * @param link 请求的API链接
     * @return 返回JSON数据
     */
    public String apiClient(String link) {
        try {
            // 天行数据api密钥
            String KEY = "a3b684c00a5a8eac15e06653f1b52ca8";
            // 获取URL（拼接上密钥）
            URL url = new URL(link + "?key=" + KEY);
            // 开启连接
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 调用工具类得到JSON数据
            StringBuilder stringBuilder = HttpConUtil.requestCon(con);
            // 返回JSON数据
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("调用API出现错误");
        }
    }
}
