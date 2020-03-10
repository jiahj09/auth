package com.example.auth_api.utils;


import com.example.auth_comm.constant.ParamEnum;
import com.example.auth_comm.utils.TaskUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 22:33
 */
@SpringBootTest
public class TaskUtilsTest {
    @Autowired
    TaskUtils taskUtils;

    String task_id = "48301f38-6baa-4590-b332-a01c1c6a43cd";

    @Test
    public void createTaskInfo() {
        task_id = UUID.randomUUID().toString();
        String type = "operator";
        taskUtils.createTaskInfo(task_id, type);
    }


    @Test
    public void testSetStatusInput() {
        taskUtils.setStatusInput(task_id, new ArrayList<ParamEnum>() {{
            add(ParamEnum.PHONE);
            add(ParamEnum.LOGIN_PASSWORD);
        }});
    }
    @Test
    public void testGetStatus(){
        String status = taskUtils.getStatus(task_id);
        System.out.println(status);
    }
}