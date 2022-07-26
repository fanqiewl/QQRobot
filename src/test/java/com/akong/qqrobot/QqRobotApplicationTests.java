package com.akong.qqrobot;

import com.akong.qqrobot.annotation.GroupFilter;
import com.akong.qqrobot.util.HttpConUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.HttpURLConnection;
import java.net.URL;


@SpringBootTest
class QqRobotApplicationTests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://api.loliurl.club/api/se?ttu/ags=r18");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String json = HttpConUtil.requestCon(con).toString();
        json = json.substring(0, json.indexOf("\"tags\"")) + json.substring(json.indexOf("\"url\""));
        System.out.println(json);
        System.out.println(objectMapper.readTree(json).get("url").asText());
    }

    @Test
    @GroupFilter
    void contextLoads() {
        
    }

}
