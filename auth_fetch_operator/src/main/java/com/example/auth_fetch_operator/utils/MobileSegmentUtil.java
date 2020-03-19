package com.example.auth_fetch_operator.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 功能：用于查询手机号码归属地等详细信息
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 13:13
 */
@Component
public class MobileSegmentUtil {


    public JSONObject mobileSegment(String mobile) {
        // TODO  有待完善请求方式
        Connection.Response execute = null;
        try {
            execute = Jsoup.connect("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + mobile)
                    .ignoreContentType(true).ignoreHttpErrors(true).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String body = execute.body();
        String trim = body.replace("__GetZoneResult_ = ", "").trim();
        JSONObject jsonObject = JSONObject.parseObject(trim);
        String catName = jsonObject.getString("catName");
        String carrier = jsonObject.getString("carrier");
        return new JSONObject() {{
            put("carrier", carrier);
            put("catName", catName);
        }};
    }
}
