package com.example.auth_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.example")
public class AuthApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(AuthApiApplication.class, args);
    }

}
