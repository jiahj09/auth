package com.example.auth_api.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/7 16:19
 */
//@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeServiceTest {
    @Autowired
    TypeService typeService;

    @Test
    public void getAllSupportType() {
        typeService.getAllSupportType();

    }
}