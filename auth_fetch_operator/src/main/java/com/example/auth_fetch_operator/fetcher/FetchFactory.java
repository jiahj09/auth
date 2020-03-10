package com.example.auth_fetch_operator.fetcher;

import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_fetch_operator.AuthFetchOperatorApplication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:13
 */
@Component
public class FetchFactory extends BasicFetcher{

    public BasicFetcher getFetcher(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        // TODO 运营商通过phone 来 区分其service信息

        Object bean = AuthFetchOperatorApplication.applicationContext.getBean("beijing_telecom");
        return (BasicFetcher) bean;
    }


    @Override
    public void init(String task_id) {
        taskUtils.setStatusInput(task_id, new ArrayList<ParamEnum>() {{
            add(ParamEnum.PHONE);
            add(ParamEnum.LOGIN_PASSWORD);
        }});
        taskUtils.setNextStep(task_id, StepEnum.LOGIN);
    }

    @Override
    public void login(String task_id) {
        getFetcher(task_id).login(task_id);
    }

    @Override
    public void login_sms(String task_id) {
        getFetcher(task_id).login_sms(task_id);
    }

    @Override
    public void base(String task_id) {
        getFetcher(task_id).base(task_id);
    }

    @Override
    public void base_sms(String task_id) {
        getFetcher(task_id).base_sms(task_id);
    }

    @Override
    public void bill(String task_id) {
        getFetcher(task_id).bill(task_id);
    }

    @Override
    public void bil_sms(String task_id) {
        getFetcher(task_id).bil_sms(task_id);
    }

    @Override
    public void call(String task_id) {
        getFetcher(task_id).call(task_id);
    }

    @Override
    public void call_sms(String task_id) {
        getFetcher(task_id).call_sms(task_id);
    }
}
