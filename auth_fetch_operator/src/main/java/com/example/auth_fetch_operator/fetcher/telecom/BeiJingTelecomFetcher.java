package com.example.auth_fetch_operator.fetcher.telecom;

import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.constant.StepEnum;
import com.example.auth_fetch_operator.fetcher.BasicFetcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/8 11:19
 */
@Component("beijing_telecom")
public class BeiJingTelecomFetcher extends BasicFetcher {

    @Override
    public void login(String task_id) {
        String phone = taskUtils.getInputParam(task_id, ParamEnum.PHONE);
        String loginPassword = taskUtils.getInputParam(task_id, ParamEnum.LOGIN_PASSWORD);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (loginPassword != null && loginPassword.equals("123456")) {
            taskUtils.setStatusDone(task_id, "授权成功！");
        } else {
            taskUtils.setMsg(task_id, "密码输入错误！");
            taskUtils.setStatusInput(task_id, new ArrayList<ParamEnum>() {{
                add(ParamEnum.LOGIN_PASSWORD);
            }});
            taskUtils.setNextStep(task_id, StepEnum.LOGIN);
        }
    }
}
