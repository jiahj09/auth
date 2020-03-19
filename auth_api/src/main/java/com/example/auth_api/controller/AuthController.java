package com.example.auth_api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_api.domain.Response;
import com.example.auth_api.service.TaskService;
import com.example.auth_api.service.TypeService;
import com.example.auth_comm.constant.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 功能：授权对外web api
 *
 * @Author:JIUNLIU
 * @data : 2020/3/6 9:38
 */
@CrossOrigin
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
     * 查询所有可用授权类型，对应到当前用户所支持的列表
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
        Response response;
        boolean b = typeCheck(type);
        if (b) {
            String task_id = taskService.createTask(type);
            response = new Response(task_id, StatusEnum.SUCCESS, type + "类型任务，初始化创建成功！");
        } else {
            response = new Response(null, StatusEnum.ERROR, type + "输入错误，当前type，不受支持~");
        }
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

        Response response;
        JSONObject paramObject = JSONObject.parseObject(requestBody);
        String validate = paramValidate(task_id, paramObject);
        if (validate == null || validate.equalsIgnoreCase("")) {
            response = taskService.input(task_id, paramObject);
        } else {
            response = new Response(task_id, StatusEnum.ERROR, validate);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    /**
     * 授权项目，是否受到支持。~~~
     *
     * @param type
     * @return
     */
    public boolean typeCheck(String type) {
        Object allSupportType = typeService.getAllSupportType();
        String s = JSONObject.toJSONString(allSupportType);
        JSONArray objects = JSONArray.parseArray(s);
        List<String> avTypes = new ArrayList<>();
        Stack<String> tempStack = new Stack<>();
        typeService.findAllType(objects, avTypes, tempStack);

        if (avTypes.contains(type)) return true;
        else return false;
    }

    public String paramValidate(String task_id, JSONObject paramObject) {
        String result = "";
        JSONArray allNeedParam = taskService.getAllNeedParam(task_id);
        for (int i = 0; i < allNeedParam.size(); i++) {
            JSONObject jsonObject = allNeedParam.getJSONObject(i);
            String key = jsonObject.getString("key");
            String value = paramObject.getString(key);
            if (value == null) {
                result += key + "不能为空！\n";
            }//TODO 按照给出的正则做出对应的校验
        }
        return result;
    }
}
