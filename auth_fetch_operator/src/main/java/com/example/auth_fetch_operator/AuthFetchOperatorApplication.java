package com.example.auth_fetch_operator;

import webspider.utils.ContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.example")
@ComponentScan(value = "webspider")
public class AuthFetchOperatorApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AuthFetchOperatorApplication.class, args);
        ContextUtil.setApp(applicationContext);
    }


}
