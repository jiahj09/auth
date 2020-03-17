package webspider.utils;

import com.alibaba.fastjson.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import webspider.http.cookie.Cookie;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 19:54
 */
class FetchUtilTest {


    @Test
    public void testCookie() throws Exception {
        Connection.Response response1 = Jsoup.connect("https://login.10086.cn/loadSendflag.htm?timestamp=")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(Connection.Method.GET)
                .headers(new HashMap<String, String>() {{
                    put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
                    put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
                    put("Referer", "https://login.10086.cn/");
                    put("Sec-Fetch-Dest", "image");
                    put("Sec-Fetch-Site", "same-origin");
                    put("Host", "login.10086.cn");
                    put("Accept-Encoding", "gzip, deflate, br");
                    put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7");
                    put("Sec-Fetch-Mode", "no-cors");
                }})
                .execute();
        String body1 = response1.body();

        Class<?> clazz = response1.getClass();
        Class<?> superclass = clazz.getSuperclass();
        Field headers = superclass.getDeclaredField("headers");
        headers.setAccessible(true);
        Map map = (Map) headers.get(response1);
        List<String> strings = (List<String>) map.get("Set-Cookie");


        String s = strings.get(0);


//        List<webspider.http.cookie.Cookie> cookies = CookieStore.parseHeader2Cookies(strings);
//        BasicClientCookie
        System.out.println("");
//        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {//向上循环 遍历父类
//            Field[] field = clazz.getDeclaredFields();
//            for (Field f : field) {
//                f.setAccessible(true);
//                Object o = f.get(response1);
//                if (o != null)
//                    System.out.println("属性：" + f.getName() + " 值：" + o.toString());
//                else System.out.println("属性：" + f.getName() + " 值：" + null);
//
//            }
//        }
    }

    @Test
    public void testHeader2Cookie() throws Exception {
        //sendflag=20200317100827823356;domain=.10086.cn;secure;HTTPOnly;
        String origin = "sendflag=20200317100827823356;domain=;secure;HTTPOnly;";

        Cookie cookie = new Cookie();
        boolean nameFlag = true;
        while (true) {
            int i = origin.indexOf(";");
            if (i == -1 || i == origin.length() - 1) break;
            String substring = origin.substring(0, i).trim();
            if (substring.contains("=")) {
                int indexOf = substring.indexOf("=");
                String key = substring.substring(0, indexOf);
                String value = substring.substring(indexOf + 1);
                if (nameFlag) {
                    cookie.setName(key);
                    cookie.setValue(value);
                    nameFlag = false;
                } else if (key.startsWith("domain")) {
                    cookie.setCookieDomain(value);
                } else if (key.startsWith("path")) {
                    cookie.setCookiePath(value);
                }
            }
            origin = origin.substring(i + 1).trim();
        }
        cookie.setCookieExpiryDate(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        if (cookie.getCookiePath() == null) cookie.setCookiePath("/");
        System.out.println("");
    }


    @Test
    public void testParseTime() throws Exception {
        long l = System.currentTimeMillis() + (1000 * 60 * 30);
        System.out.println(new Date(l));

    }
}