package webspider.utils;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.Arg;
import org.apache.http.util.Args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import webspider.http.cookie.Cookie;
import webspider.http.cookie.CookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 18:44
 */
@Component
public class FetchUtil {

    String fetchInfoKeySuffix = "_fetch_info";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public CookieStore getCookies(String task_id) {
        Object cookieStr = redisTemplate.opsForHash().get(getFetchKey(task_id), "cookies");
        if (cookieStr == null) return null;
        String s = cookieStr.toString();
        CookieStore cookieStore = JSONObject.parseObject(s, CookieStore.class);
        return cookieStore;
    }


    public void addCookie(String task_id, String name, String value) {
        Args.notNull(task_id, "task_id");
        Args.notNull(name, "name");
        Cookie cookie = new Cookie();
        cookie.setName(name);
        if (value == null) cookie.setValue("");
        else
            cookie.setValue(value);
        addCookie(task_id, cookie);
    }

    public void addCookie(String task_id, Cookie cookie) {
        addCookies(task_id, new ArrayList<Cookie>() {{
            add(cookie);
        }});
    }

    public void addCookies(String task_id, List<Cookie> cookies) {
        Args.notNull(task_id, "task_id");
        Args.notNull(cookies, "cookies");

        CookieStore cookieStore = getCookies(task_id);
        if (cookieStore == null) {
            cookieStore = new CookieStore();
            cookieStore.setCookies(cookies);
        }
        else {
            for (Cookie cookie : cookies) {
                if (!cookieStore.isContain(cookie)) cookieStore.addCookie(cookie);
            }
        }

        updateCookies(task_id, cookieStore);
    }

    public void updateCookies(String task_id, CookieStore cookies) {
        String s = JSONObject.toJSONString(cookies);
        redisTemplate.opsForHash().put(getFetchKey(task_id), "cookies", s);
    }


    public void setField(String task_id, String key, String value) {
        redisTemplate.opsForHash().put(getFetchKey(task_id), key, value);
    }

    public String getField(String task_id, String key) {
        Object o = redisTemplate.opsForHash().get(getFetchKey(task_id), key);
        if (o == null)
            return null;
        else return o.toString();
    }

    private String getFetchKey(String task_id) {
        return task_id + fetchInfoKeySuffix;
    }
}
