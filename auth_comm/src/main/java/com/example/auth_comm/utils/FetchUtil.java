package com.example.auth_comm.utils;

import com.example.auth_comm.constant.FetchStatusEnum;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


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
        BasicCookieStore cookieStore = ObjectUtils.base642Obj(s, BasicCookieStore.class);
        return cookieStore;
    }


    public void addCookie(String task_id, Cookie cookie) {
        CookieStore cookies = getCookies(task_id);
        cookies.addCookie(cookie);
        updateCookies(task_id, cookies);
    }


    public void setFetchStatus(String task_id, String dataKey, FetchStatusEnum statusEnum) {
        redisTemplate.opsForHash().put(getFetchKey(task_id), dataKey, statusEnum.toString());
        redisTemplate.expire(getFetchKey(task_id), 15, TimeUnit.MINUTES);
    }


    /**
     * 使用Httpclient 进行下载时，cookie 直接使用更新的方式
     *
     * @param task_id
     * @param cookies
     */
    public void updateCookies(String task_id, CookieStore cookies) {
        String s = ObjectUtils.toBase64(cookies);
        redisTemplate.opsForHash().put(getFetchKey(task_id), "cookies", s);
        redisTemplate.expire(getFetchKey(task_id), 15, TimeUnit.MINUTES);

    }


    public void setField(String task_id, String key, String value) {
        redisTemplate.opsForHash().put(getFetchKey(task_id), key, value);
        redisTemplate.expire(getFetchKey(task_id), 15, TimeUnit.MINUTES);
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
