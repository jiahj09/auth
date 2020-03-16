package com.example.auth_api.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_api.service.TypeService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 9:24
 */
@SpringBootTest
class SupportSchedulerTest {
    private static final Logger logger = LoggerFactory.getLogger(SupportSchedulerTest.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TypeService typeService;

    @Test
    void synAllSupportType() {
        Object allSupportType = typeService.getAllSupportType();
        if (allSupportType == null) {
            logger.info("msg=授权支持列表查询错误");
        } else {
            String string = JSONObject.toJSONString(allSupportType);

            List<String> allTypes = new ArrayList<>();
            Stack<String> typeTempStack = new Stack<>();
            JSONArray array = JSONArray.parseArray(string);
            findAllType(array, allTypes, typeTempStack);
            System.out.println(allTypes);
        }
    }

    void findAllType(JSONArray data, List<String> allTypes, Stack<String> typeStackTemp) {
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            String type = jsonObject.getString("type");
            if (type != null) typeStackTemp.push(type);
            boolean child_flag = jsonObject.getBooleanValue("child_flag");
            if (child_flag) { // 有子节点
                JSONArray child_prop = jsonObject.getJSONArray("child_prop");
                findAllType(child_prop, allTypes, typeStackTemp);
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
                typeStackTemp.pop();
            }
        }
    }
}