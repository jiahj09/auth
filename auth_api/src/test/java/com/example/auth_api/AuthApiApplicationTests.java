package com.example.auth_api;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@RunWith(SpringRunner.class)
@SpringBootTest
class AuthApiApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    String config_collection_name = "auth_config";

    @Test
    void contextLoads() {
        JSONObject typeInfo = new JSONObject();
        typeInfo.put("config_name", "type_config");
        typeInfo.put("create_time", new Date());
        typeInfo.put("update_time", new Date());
        typeInfo.put("create_by", "JIUN·LIU");
        typeInfo.put("last_update_by", "JIUN·LIU");


        List<JSONObject> prop = new ArrayList<>();

        prop.add(new JSONObject() {{
            put("type", "operator");
            put("name", "运营商");
            put("child", new ArrayList<String>() {{
                add("上海电信");
                add("北京电信");
            }});
        }});

        typeInfo.put("prop", prop);

        mongoTemplate.save(typeInfo, config_collection_name);
    }

}
