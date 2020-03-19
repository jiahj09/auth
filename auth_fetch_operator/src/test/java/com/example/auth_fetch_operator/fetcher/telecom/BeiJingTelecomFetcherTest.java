package com.example.auth_fetch_operator.fetcher.telecom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/18 13:55
 */
@SpringBootTest
class BeiJingTelecomFetcherTest {
    @Autowired()
    BeiJingTelecomFetcher beiJingTelecomFetcher;

    @Test
    void base() {
        new Thread(() -> {
            beiJingTelecomFetcher.call_info(UUID.randomUUID().toString());
        }).start();

        System.out.println("");
    }
}