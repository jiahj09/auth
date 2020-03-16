package com.example.auth_fetch_operator.fetcher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 14:10
 */
@SpringBootTest
class FetchFactoryTest {
    @Autowired
    FetchFactory factory;

    @Test
    void getCarrierBeans() {
        List<String> carrierBeans = factory.getCarrierBeans("北京移动");
        System.out.println(carrierBeans);

    }

    @Test
    void getPhoneOwenBeanName() {
        String phoneOwenBeanName = factory.getPhoneOwenBeanName("17610757597");
        System.out.println(phoneOwenBeanName);
    }
}