package com.example.auth_fetch_operator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 功能：
 * spring异步执行线程池配置
 *
 * @Author:JIUNLIU
 * @data : 2020/3/19 10:42
 */
@Component
public class ExecutorsConfig {


    private static final int corePoolSize = 5;            // 核心线程数（默认线程数）
    private static final int maxPoolSize = 8;                // 最大线程数
    private static final int keepAliveTime = 10;            // 允许线程空闲时间（单位：默认为秒）
    private static final int queueCapacity = 3;            // 缓冲队列数,尽量不往队列中存放，实际的开始执行
    private static final String threadNamePrefix = "owen_executor"; // 线程池名前缀


    @Bean("owen_executor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
