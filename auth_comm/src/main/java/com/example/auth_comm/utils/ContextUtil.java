package com.example.auth_comm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 18:49
 */
@Component
public class ContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    public static <T> T getObj(Class<T> tClass) {
        return context.getBean(tClass);
    }

    public static Object getObj(String beanName) {
        return context.getBean(beanName);
    }

    public static void setApp(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
