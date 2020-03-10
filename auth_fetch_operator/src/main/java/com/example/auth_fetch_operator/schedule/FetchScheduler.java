package com.example.auth_fetch_operator.schedule;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_comm.utils.TaskUtils;
import com.example.auth_fetch_operator.fetcher.FetchFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能：
 * 用于接收mq数据并进行处理
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:23
 */
@Component
public class FetchScheduler {
    private static final Logger logger = LoggerFactory.getLogger(FetchScheduler.class);

    @Autowired
    TaskUtils taskUtils;
    @Autowired
    FetchFactory fetchFactory;

    @RabbitListener(queues = "${mq.operator.queue.name}")
    public void scheduler(String msg) {
        JSONObject msgObj = JSONObject.parseObject(msg);
        String task_id = msgObj.getString(taskUtils.getTask_id_key());
        String next_step = msgObj.getString(taskUtils.getNext_step_key());
        StepEnum stepEnum = StepEnum.getStepByValue(next_step);

        if (stepEnum == null) {
            logger.info("task_id = {} , msg={}", task_id, "next_step 设置错误");
            taskUtils.setStatusError(task_id, "fetcher 系统异常！");
            return;
        }
        switch (stepEnum) {
            case INIT:
                fetchFactory.init(task_id);
                break;
            case LOGIN:
                fetchFactory.login(task_id);
                break;
            case LOGIN_SMS:
                fetchFactory.login_sms(task_id);
                break;
            case BASE:
                fetchFactory.base(task_id);
                break;
            case BASE_SMS:
                fetchFactory.base_sms(task_id);
                break;
            case BILL:
                fetchFactory.bill(task_id);
                break;
            case BILL_SMS:
                fetchFactory.bil_sms(task_id);
                break;
            case CALL:
                fetchFactory.call(task_id);
                break;
            case CALL_SMS:
                fetchFactory.call_sms(task_id);
                break;
        }

    }
}
