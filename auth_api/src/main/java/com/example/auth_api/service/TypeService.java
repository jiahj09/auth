package com.example.auth_api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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


    /**
     * 支持列表查询
     * @return
     */
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

    /**
     * 用于得出所有被支持的type 列表
     * @param data 支持列表，getAllSupportType返回值
     * @param allTypes 用于接收所有支持列表的容器
     * @param typeStackTemp 用于递归的零时存储栈
     */
    public void findAllType(JSONArray data, List<String> allTypes, Stack<String> typeStackTemp) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            String type = jsonObject.getString("type");
            if (type != null) typeStackTemp.push(type);
            boolean child_flag = jsonObject.getBooleanValue("child_flag");
            if (child_flag) { // 有子节点
                JSONArray child_prop = jsonObject.getJSONArray("child_prop");
                findAllType(child_prop, allTypes, typeStackTemp); // 进行递归查找
            } else {
                // 没有子节点，拼接所有的type
                Iterator<String> iterator = typeStackTemp.iterator();
                String allName = "";
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    if (allName.equalsIgnoreCase("")) {
                        allName = next;
                    } else {
                        allName = allName + "_" + next;
                    }
                }
                allTypes.add(allName);
                typeStackTemp.pop(); // 用于回溯，抛出最外层节点信息。
            }
        }
    }
}
