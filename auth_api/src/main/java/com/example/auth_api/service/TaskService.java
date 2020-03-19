package com.example.auth_api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_api.domain.Response;
import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StatusEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_comm.domain.MSGInfo;
import com.example.auth_comm.utils.TaskUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 功能：
 * 任务的一些基础功能
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 16:42
 */
@Service
public class TaskService {

    TaskUtils taskUtils;

    @Autowired
    public TaskService(TaskUtils taskUtils) {
        this.taskUtils = taskUtils;
    }

    public String createTask(String type) {
        String task_id = UUID.randomUUID().toString();
        taskUtils.createTaskInfo(task_id, type);
        taskUtils.pushMQ(new MSGInfo(type, task_id, StepEnum.INIT.toString()));
        return task_id;
    }

    public Response getStatus(String task_id) {
        String status = taskUtils.getStatus(task_id);
        if (status == null) return new Response(task_id, StatusEnum.ERROR, "当前task_id，不存在！");
        String msg = taskUtils.getMsg(task_id);
        Object needInputField = taskUtils.getNeedInputField(task_id);


        StatusEnum statusEnum = StatusEnum.DOING;
        if (status.equals(StatusEnum.DOING.toString())) {
            statusEnum = StatusEnum.DOING;
        } else if (status.equals(StatusEnum.ERROR.toString())) {
            statusEnum = StatusEnum.ERROR;
        } else if (status.equals(StatusEnum.DONE.toString())) {
            statusEnum = StatusEnum.DONE;
        } else if (status.equals(StatusEnum.INPUT.toString())) {
            statusEnum = StatusEnum.INPUT;
        }
        Response response = new Response(task_id, statusEnum, msg);
        if (statusEnum == StatusEnum.INPUT)
            response.setData(needInputField);
        else
            response.setData(null);

        return response;
    }


    public Response input(String task_id, JSONObject param) {
        String msg;
        String status = taskUtils.getStatus(task_id);
        if (status == null) return new Response(task_id, StatusEnum.ERROR, "当前task_id，不存在！");
        if (status.equals(StatusEnum.INPUT.toString())) {
            taskUtils.setMsg(task_id, null);
            taskUtils.setInputParams(task_id, param);
            taskUtils.setStatusDoing(task_id);
            msg = taskUtils.getMsg(task_id);
            String nextStep = taskUtils.getNextStep(task_id);
            String type = taskUtils.getType(task_id);
            taskUtils.pushMQ(new MSGInfo(type, task_id, nextStep));
        } else {
            msg = "当前非输入状态，不可进行信息输入！";
        }

        Response response = new Response(task_id, StatusEnum.SUCCESS, msg);
        return response;
    }


    public JSONArray getAllNeedParam(String task_id) {
        Object needInputField = taskUtils.getNeedInputField(task_id);
        if (needInputField == null) return null;
        else {
            return (JSONArray) needInputField;
        }
    }

}
