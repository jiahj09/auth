package com.example.auth_api.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_api.domain.Response;
import com.example.auth_api.service.TaskService;
import com.example.auth_api.service.TypeService;
import com.example.auth_comm.constant.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 功能：授权对外web api
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 9:38
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    private TaskService taskService;
    private TypeService typeService;

    @Autowired
    public AuthController(TaskService taskService, TypeService typeService) {
        this.taskService = taskService;
        this.typeService = typeService;
    }


    /**
     * 查询所有可用授权类型
     * <p>
     * 用于解析数据内容：
     *
     * @return [
     * {
     * "type": "taobao",
     * "name": "淘宝授权"
     * },
     * {
     * "type": "operator",
     * "name": "运营商",
     * "child": [
     * "北京移动",
     * "上海电信"
     * ]
     * },
     * {
     * "type": "insurance",
     * "name": "社保"
     * }
     * ]
     */
    @RequestMapping(value = "support")
    public ResponseEntity<Response> available() {
        Object allSupportType = typeService.getAllSupportType();
        Response response = new Response(null, StatusEnum.SUCCESS, "支持项目列表查询成功！");
        response.setData(allSupportType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "init/{type}")
    public ResponseEntity<Response> init(@PathVariable String type) {
        // TODO type 校验
        String task_id = taskService.createTask(type);
        Response response = new Response(task_id, StatusEnum.SUCCESS, type + "类型任务，初始化创建成功！");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "status/{task_id}")
    public ResponseEntity<Response> status(@PathVariable String task_id) {
        Response response = taskService.getStatus(task_id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "input/{task_id}", method = RequestMethod.POST)
    public ResponseEntity<Response> input(@PathVariable String task_id,
                                          @RequestBody String requestBody) {

        // TODO  param 校验
        JSONObject paramObject = JSONObject.parseObject(requestBody);

        Response response = taskService.input(task_id, paramObject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
