package com.example.auth_comm.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StatusEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_comm.domain.MSGInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 22:26
 */
@Component
public class TaskUtils {
    private static final Logger logger = LoggerFactory.getLogger(TaskUtils.class);

    @Value("${auth.taskinfo.task_id.key}")
    private String task_id_key;

    @Value("${auth.taskinfo.fetch_info.key}")
    private String fetch_info_key;

    @Value("${auth.taskinfo.input_fields.key}")
    private String input_fields_key;

    @Value("${auth.taskinfo.input_params.key}")
    private String input_params_key;

    @Value("${auth.taskinfo.status.key}")
    private String status_key;

    @Value("${auth.taskinfo.next_step.key}")
    private String next_step_key;

    @Value("${auth.taskinfo.type.key}")
    private String type_key;

    @Value("${auth.taskinfo.msg.key}")
    private String msg_key;

    @Value("${mq.all.queue.suffix}")
    private String queue_suffix;

    @Value("${spring.redis.expire_seconds}")
    private long expire_seconds;

    StringRedisTemplate redisTemplate;

    RabbitTemplate rabbitTemplate;

    @Autowired
    public TaskUtils(StringRedisTemplate redisTemplate, RabbitTemplate rabbitTemplate) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void pushMQ(MSGInfo msgInfo) {
        String QUEUE = msgInfo.getType().toUpperCase();
        String queueName = QUEUE + queue_suffix;
        logger.info("task_id = {} ,msg= push 信息到 {}。", msgInfo.getTask_id(), queueName);
        rabbitTemplate.convertAndSend(queueName, msgInfo.toString());
    }

    /**
     * 创建一个task_info
     * 设置初始化值
     *
     * @param task_id
     * @param type
     */
    public void createTaskInfo(String task_id, String type) {
        redisTemplate.opsForHash().put(task_id, type_key, type);
        redisTemplate.opsForHash().put(task_id, next_step_key, StepEnum.INIT.toString());
        setStatusDoing(task_id);
        redisTemplate.expire(task_id, expire_seconds, TimeUnit.SECONDS);
        logger.info("create task_info: task_id={} , next_step ={}", task_id, StepEnum.INIT);
    }

    public void setStatusDoing(String task_id) {
        redisTemplate.opsForHash().put(task_id, status_key, StatusEnum.DOING.toString());
        logger.info("set status DOING: task_id={}");
    }

    public void setStatusInput(String task_id, List<ParamEnum> paramEnums) {
        // 需要 api 输入的信息，不进行累增，每次刷新
        redisTemplate.opsForHash().put(task_id, input_fields_key, paramEnums.toString());
        redisTemplate.opsForHash().put(task_id, status_key, StatusEnum.INPUT.toString());
        logger.info("set status INPUT: task_id={} , need fields = {}", task_id, paramEnums);
    }

    public void setStatusDone(String task_id, String msg) {
        redisTemplate.opsForHash().put(task_id, msg_key, msg);
        redisTemplate.opsForHash().delete(task_id, input_fields_key);
        redisTemplate.opsForHash().put(task_id, status_key, StatusEnum.DONE.toString());
        logger.info("set status DONE: task_id={} , msg = {}", task_id, msg);
    }

    public void setStatusError(String task_id, String msg) {
        redisTemplate.opsForHash().put(task_id, msg_key, msg);
        redisTemplate.opsForHash().put(task_id, status_key, StatusEnum.ERROR.toString());
        logger.info("set status ERROR: task_id={} , msg={}", task_id, msg);
    }

    /**
     * 给redis中设置输入参数信息，采用累增的方式添加
     *
     * @param task_id
     * @param params
     */
    public void setInputParams(String task_id, JSONObject params) {
        JSONObject inputParams = getInputParams(task_id);
        if (inputParams == null) inputParams = new JSONObject();
        inputParams.putAll(params);
        redisTemplate.opsForHash().put(task_id, input_params_key, inputParams.toJSONString());
    }


    public void setNextStep(String task_id, StepEnum stepEnum) {
        redisTemplate.opsForHash().put(task_id, next_step_key, stepEnum.toString());
    }

    public String getNextStep(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, next_step_key);
        return o.toString();
    }

    public String getType(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, type_key);
        return o.toString();
    }

    public String getStatus(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, status_key);
        if (o == null) return null;
        return o.toString();
    }

    public String getMsg(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, msg_key);
        if (o == null) return null;
        return o.toString();
    }

    public void setMsg(String task_id, String msg) {
        if (msg == null || msg.trim().equals("")) {
            redisTemplate.opsForHash().delete(task_id, msg_key);
        } else {
            redisTemplate.opsForHash().put(task_id, msg_key, msg);
        }

    }

    public Object getNeedInputField(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, input_fields_key);
        if (o == null) return null;
        JSONArray objects = JSONArray.parseArray(o.toString());
        return objects;
    }

    public JSONObject getInputParams(String task_id) {
        Object o = redisTemplate.opsForHash().get(task_id, input_params_key);
        if (o == null) return null;
        JSONObject paramObject = JSONObject.parseObject(o.toString());
        return paramObject;
    }

    public String getInputParam(String task_id, ParamEnum paramEnum) {
        JSONObject inputParams = getInputParams(task_id);
        if (inputParams == null) return null;
        return inputParams.getString(paramEnum.getKey());
    }


    public String getFetch_info_key() {
        return fetch_info_key;
    }

    public String getInput_fields_key() {
        return input_fields_key;
    }

    public String getInput_params_key() {
        return input_params_key;
    }

    public String getStatus_key() {
        return status_key;
    }

    public String getNext_step_key() {
        return next_step_key;
    }

    public String getType_key() {
        return type_key;
    }

    public String getMsg_key() {
        return msg_key;
    }

    public String getTask_id_key() {
        return this.task_id_key;
    }

}
