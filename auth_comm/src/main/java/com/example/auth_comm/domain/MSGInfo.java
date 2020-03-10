package com.example.auth_comm.domain;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能：
 * mq 信息推送实体
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 14:20
 */
public class MSGInfo {

    String type;
    String task_id;
    String next_step;

    public MSGInfo(String type, String task_id, String next_step) {
        this.type = type;
        this.task_id = task_id;
        this.next_step = next_step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getNext_step() {
        return next_step;
    }

    public void setNext_step(String next_step) {
        this.next_step = next_step;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
