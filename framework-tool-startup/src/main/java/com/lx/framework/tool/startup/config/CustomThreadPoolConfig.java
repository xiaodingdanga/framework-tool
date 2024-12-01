package com.lx.framework.tool.startup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Description: 自定义线程池
 * Date: 2022/4/23 16:07
 * @author xin.liu
 * @version 1.0.0
 */
@Configuration
@EnableAsync
public class CustomThreadPoolConfig {

    /*
     * IO密集型配置cpu*2，CPU密集型配置略大于cpu数量 cpu+1
     */
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final int MAX_SIZE = 20;
    private static final int QUEUE_SIZE = 50;
    private static final int KEEP_ALIVE_SECOND = 60;

    @Bean("customizeThreadPool")
    public Executor doConfigCustomizeThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(CORE_SIZE);
        //最大线程数
        executor.setMaxPoolSize(MAX_SIZE);
        //队列容量
        executor.setQueueCapacity(QUEUE_SIZE);
        //活跃时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECOND);
        //线程名字前缀
        executor.setThreadNamePrefix("customize-thread-");
        /*
         *线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
         *表示当线程池没有处理能力的时候，该策略会直接在execute方法的调用线程中运行这个任务；
         *如果执行程序已关闭，则会丢弃该任务
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}