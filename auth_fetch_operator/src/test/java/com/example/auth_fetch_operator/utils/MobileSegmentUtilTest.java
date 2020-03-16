package com.example.auth_fetch_operator.utils;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 13:15
 */
class MobileSegmentUtilTest {

    @Test
    void mobileSegment() {
        JSONObject jsonObject = new MobileSegmentUtil().mobileSegment("17345113254");
        System.out.println(jsonObject);
    }

    @Test
    void randomTest() {
        for (int i = 0; i < 20; i++)
            System.out.println();
    }
}