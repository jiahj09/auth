package webspider.domain;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * 功能：
 * 用于爬虫存储中间信息的实体类型
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 22:20
 */
public class FetchInfo {

    JSONObject fields;// 相关字段存储
    String fetchBean;
    List<Cookie> cookies;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
