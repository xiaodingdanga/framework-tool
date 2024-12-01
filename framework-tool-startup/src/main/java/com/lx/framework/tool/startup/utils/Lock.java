package com.lx.framework.tool.startup.utils;

import com.lx.framework.tool.redis.ApplicationContextHolder;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-24  14:37
 * @Version 1.0
 */
@Data
public class Lock implements Runnable {

    private static StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) ApplicationContextHolder.getBean("stringRedisTemplate");
    private String key;
    private String value;
    private int timeout;
    private Thread thread;

    public Lock(String key, String value, int timeout) {
        this.key = key;
        this.value = value;
        this.timeout = timeout;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    /**
     * @description 每隔一秒给锁续期
     * @return: void
     * @author xin.liu
     * @date  17:57
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (!thread.isInterrupted()) {
            long current = System.currentTimeMillis();
            //续约时间必须小于加锁时间，否则会出现第二个线程获取锁的时候第一个线程的锁刚好失效，但看门狗并未续约成功。
            if (current - start >= 2000) {
                stringRedisTemplate.opsForValue().getAndExpire(key, timeout, TimeUnit.SECONDS);
                System.out.println(thread.getName()+":续期。。。");
                start = current;
            }
        }
    }
}