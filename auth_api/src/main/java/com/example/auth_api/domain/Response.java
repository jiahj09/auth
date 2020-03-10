package com.example.auth_api.domain;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.StatusEnum;

/**
 * 功能：返回信息实体
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 16:34
 */
public class Response {
    private String task_id;

    private Object data;

    private String msg;

    private StatusEnum status;

    public Response(String task_id, StatusEnum status, String msg) {
        this.task_id = task_id;
        this.status = status;
        this.msg = msg;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, false);
    }
}
