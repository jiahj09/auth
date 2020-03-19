package com.example.auth_fetch_operator.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_fetch_operator.domain.BaseInfo;
import com.example.auth_fetch_operator.domain.BillInfo;
import com.example.auth_fetch_operator.domain.CallInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 16:28
 */
@Component
public class SaveInfoDao {

    MongoTemplate mongoTemplate;

    @Value("${auth.operator.type.key}")
    private String typeKey;
    @Value("${auth.operator.base.type}")
    private String baseType;
    @Value("${auth.operator.base.type}")
    private String billType;
    @Value("${auth.operator.base.type}")
    private String callType;
    @Value("${auth.operator.coll.name}")
    private String savedColl;

    @Autowired
    public SaveInfoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void saveBase(String task_id, BaseInfo baseInfo) {
        JSONObject object = new JSONObject();
        object.put("createTime", new Date());
        object.put("task_id", task_id);
        object.put(typeKey, baseType);
        object.put("data", baseInfo);
        mongoTemplate.save(object, savedColl);
    }

    public void saveBill(String task_id, List<BillInfo> billInfos) {
        JSONObject object = new JSONObject();
        object.put("createTime", new Date());
        object.put("task_id", task_id);
        object.put(typeKey, billType);
        object.put("data", billInfos);
        mongoTemplate.save(object, savedColl);
    }

    public void saveCall(String task_id, List<CallInfo> callInfos) {
        JSONObject object = new JSONObject();
        object.put("createTime", new Date());
        object.put("task_id", task_id);
        object.put(typeKey, callType);
        object.put("data", callInfos);
        mongoTemplate.save(object, savedColl);
    }
}
