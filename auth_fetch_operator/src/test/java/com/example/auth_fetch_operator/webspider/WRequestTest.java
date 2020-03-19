package com.example.auth_fetch_operator.webspider;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import webspider.WRequest;
import webspider.http.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/18 21:17
 */
@SpringBootTest
public class WRequestTest {
    private Map<String, String> commHeader = new HashMap<String, String>() {{
        put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        put("Referer", "https://login.10086.cn/");
        put("Sec-Fetch-Dest", "image");
        put("Sec-Fetch-Site", "same-origin");
        put("Host", "login.10086.cn");
        put("Accept-Encoding", "gzip, deflate, br");
        put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        put("Sec-Fetch-Mode", "no-cors");
    }};
    @Test
    public void testContext() {
        String task_id = UUID.randomUUID().toString();
        System.out.println(task_id);
        Response execute = WRequest.create(task_id)
                .connect("http://www.10010.com/")
                .maxRedirect(1)
//                .headers(commHeader)
                .execute();
        System.out.println("");
    }

    @Test
    public void testHttpClient() throws Exception{
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client3 = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpGet httpRequest3 = new HttpGet("https://login.10086.cn/genqr.htm");

        httpRequest3.setHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        httpRequest3.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        httpRequest3.setHeader("Referer", "https://login.10086.cn/");
        httpRequest3.setHeader("Sec-Fetch-Dest", "image");
        httpRequest3.setHeader("Sec-Fetch-Site", "same-origin");
        httpRequest3.setHeader("Host", "login.10086.cn");
        httpRequest3.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpRequest3.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
        httpRequest3.setHeader("Sec-Fetch-Mode", "no-cors");

        CloseableHttpResponse execute3 = client3.execute(httpRequest3);
        String body3 = EntityUtils.toString(execute3.getEntity());

        System.err.println("--------------------------" + body3);
    }


}
