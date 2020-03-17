package webspider.utils;

import org.springframework.context.ApplicationContext;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/16 18:49
 */
public class ContextUtil {
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
}
