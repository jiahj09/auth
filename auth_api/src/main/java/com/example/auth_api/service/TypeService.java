package com.example.auth_api.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能：
 * 关于支持授权项的service类
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 16:05
 */
@Service
public class TypeService extends BaseService {

    @Value("${auth.config.type.key}")
    String type_config_key;

    @Value("${auth.config.type.value}")
    String type_config_value;


    public Object getAllSupportType() {
        logger.info("------MongoDB get_all_support_type start");
        Query query = new Query(Criteria.where(type_config_key).is(type_config_value));
        List<JSONObject> jsonObjects = mongoTemplate.find(query, JSONObject.class, config_coll_name);
        logger.info("------MongoDB get_all_support_type end");
        if (jsonObjects.size() != 1) {
            throw new RuntimeException("支持列表查询错误");
        }
        JSONObject typeConfig = jsonObjects.get(0);
        Object prop = typeConfig.get("prop");

        return prop;
    }
}
