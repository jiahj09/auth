package com.example.auth_comm.domain;

import com.alibaba.fastjson.JSONObject;

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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
