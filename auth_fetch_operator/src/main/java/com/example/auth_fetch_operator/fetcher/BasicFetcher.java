package com.example.auth_fetch_operator.fetcher;

import com.example.auth_comm.utils.TaskUtils;
import com.example.auth_fetch_operator.utils.MobileSegmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * 运营商默认的方法
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:08
 */
@Component
public class BasicFetcher {

    @Autowired
    public TaskUtils taskUtils;
    @Autowired
    public MobileSegmentUtil mobileSegmentUtil;
    @Autowired
    public MongoTemplate mongoTemplate;
    @Value("${operator.channel.collname}")
    public String channel_coll_name;

    public void init(String task_id) {

    }

    public void login(String task_id) {
    }

    public void login_sms(String task_id) {
    }

    public void base(String task_id) {
    }

    public void base_sms(String task_id) {
    }

    public void bill(String task_id) {
    }

    public void bil_sms(String task_id) {
    }

    public void call(String task_id) {
    }

    public void call_sms(String task_id) {
    }

}
