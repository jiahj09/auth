package com.example.auth_fetch_operator.schedule;

import com.alibaba.fastjson.JSONObject;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_comm.domain.MSGInfo;
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

    TaskUtils taskUtils;
    FetchFactory fetchFactory;

    @Autowired
    public FetchScheduler(TaskUtils taskUtils, FetchFactory fetchFactory) {
        this.taskUtils = taskUtils;
        this.fetchFactory = fetchFactory;
    }

    @RabbitListener(queues = "${mq.operator.queue.name}")
    public void scheduler(String msg) {
        logger.info("operator fetcher receive mq msg: {}", msg);
        MSGInfo msgInfo = JSONObject.parseObject(msg, MSGInfo.class);
        String task_id = msgInfo.getTask_id();
        StepEnum stepEnum = StepEnum.getStepByValue(msgInfo.getNext_step());
        if (stepEnum == null) {
            logger.info("task_id = {} , msg={}", task_id, "next_step 设置错误");
            taskUtils.setStatusError(task_id, "fetcher 系统异常！");
            return;
        }
        switch (stepEnum) {
            case INIT:
                logger.info("task_id={}, INIT", task_id);
                fetchFactory.init(task_id);
                break;
            case LOGIN:
                logger.info("task_id={}, LOGIN", task_id);
                fetchFactory.login(task_id);
                break;
            case LOGIN_SMS:
                logger.info("task_id={}, LOGIN_SMS", task_id);
                fetchFactory.login_sms(task_id);
                break;
            case BASE:
                logger.info("task_id={}, BASE", task_id);
                fetchFactory.base_info(task_id);
                break;
            case BASE_SMS:
                logger.info("task_id={}, BASE_SMS", task_id);
                fetchFactory.base_sms(task_id);
                break;
            case BILL:
                logger.info("task_id={}, BILL", task_id);
                fetchFactory.bill_info(task_id);
                break;
            case BILL_SMS:
                logger.info("task_id={}, BILL_SMS", task_id);
                fetchFactory.bil_sms(task_id);
                break;
            case CALL:
                logger.info("task_id={}, CALL", task_id);
                fetchFactory.call_info(task_id);
                break;
            case CALL_SMS:
                logger.info("task_id={}, CALL_SMS", task_id);
                fetchFactory.call_sms(task_id);
                break;
            case CUSTOM_DEAL:
                fetchFactory.custom_deal(task_id);
                break;
        }

    }
}
