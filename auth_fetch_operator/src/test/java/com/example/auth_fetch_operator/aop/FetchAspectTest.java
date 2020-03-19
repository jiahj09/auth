package com.example.auth_fetch_operator.aop;


import com.example.auth_comm.utils.ContextUtil;
import com.example.auth_fetch_operator.fetcher.BasicFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 10:36
 */
@SpringBootTest
public class FetchAspectTest {


    @Test
    public void test() throws Exception {
        String task_id = UUID.randomUUID().toString();
        BasicFetcher cmcc_shop = (BasicFetcher) ContextUtil.getObj("cmcc_shop");
        cmcc_shop.base_info("743ca219-31df-4d0f-81ae-fc57796c8d3f");


        System.out.println("测试方法到底啦！！！！！");
    }

}