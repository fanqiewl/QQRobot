package com.akong.qqrobot.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Http流读取工具
 *
 * @author Akong
 * @since 2021/11/26 18:24
 */
public class HttpConUtil {
    /**
     * 执行URL连接并获取内容
     *
     * @param con URL连接对象
     * @return 返回内容
     * @throws Exception 抛出异常
     */
    public static StringBuilder requestCon(HttpURLConnection con) throws Exception {
        // 设置请求方式
        con.setRequestMethod("GET");

        // 设置请求时长
        con.setConnectTimeout(3000);
        con.setReadTimeout(3000);

        // 启动连接
        con.connect();

        // 响应字符串
        StringBuilder jsonString = new StringBuilder();

        // 判断是否请求成功
        if (con.getResponseCode() >= 200 && con.getResponseCode() <= 299) {
            // 获取响应流对象
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

            // 临时储存字符串
            String temp;

            // 读取流对象
            while (null != (temp = br.readLine())) {
                // 加入到响应字符串
                jsonString.append(temp);
            }

        } else if (con.getHeaderField("Location") != null) {
            jsonString.append("{'code':200,'url':'").append(con.getHeaderField("Location")).append("'}");
        } else {
            // 若请求不成功抛出异常
            throw new RuntimeException("网络异常：请重试");
        }
        return jsonString;
    }
}
