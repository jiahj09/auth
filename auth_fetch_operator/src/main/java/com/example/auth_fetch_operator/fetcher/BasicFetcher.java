package com.example.auth_fetch_operator.fetcher;

import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_comm.utils.TaskUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
    TaskUtils taskUtils;

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
