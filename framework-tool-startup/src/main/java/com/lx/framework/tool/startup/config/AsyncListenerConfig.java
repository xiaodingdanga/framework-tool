package com.lx.framework.tool.startup.config;

import com.lx.framework.tool.startup.handler.AsyncExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: 给监听器设置异步线程池对象 AsyncListenerConfiguration
 * Date: 2022/4/23 16:07
 * @author xin.liu
 * @version 1.0.0
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncListenerConfig implements AsyncConfigurer {

    /*
     * IO密集型配置cpu*2，CPU密集型配置略大于cpu数量 cpu+1
     */
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int MAX_SIZE = 20;
    private static final int QUEUE_SIZE = 50;
    private static final int KEEP_ALIVE_SECOND = 60;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor poolExecutor = new ThreadPoolTaskExecutor();
        poolExecutor.setMaxPoolSize(MAX_SIZE);
        poolExecutor.setCorePoolSize(CORE_SIZE);
        poolExecutor.setQueueCapacity(QUEUE_SIZE);
        poolExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECOND);
        /*
         *线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
         *表示当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行这个任务；
         *如果执行程序已关闭，则会丢弃该任务
         */
        poolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolExecutor.setThreadNamePrefix("framework-thread-");
        //初始化线程池
        poolExecutor.initialize();
        return poolExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}