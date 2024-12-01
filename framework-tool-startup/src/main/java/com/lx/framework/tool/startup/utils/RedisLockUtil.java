package com.lx.framework.tool.startup.utils;

import com.lx.framework.tool.startup.handler.customException.RedisLockException;
import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-23  17:32
 * @Version 1.0
 */
@Slf4j
@Getter
@Component
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 解锁脚本,防止线程将其他线程的锁释放 redis操作这个脚本会视为一个原子，保证判断和解锁的原子性
     * 防止刚判断完这个锁是自己的，然后锁过期了，在释放就会把其他服务的锁释放掉
     */
    private static String UN_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    public Lock lock(String key, String value, int timeout) {
        boolean success = false;
        long start = System.currentTimeMillis();

        // 加锁等待时间 20s
        while (!success && System.currentTimeMillis() - start <= 5000) {
            success = stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
        }

        if (!success) {
            //抛出自定义异常 用全局异常拦截，进行获取分布式锁获取失败处理
            throw new RedisLockException(CodeEnum.REDIS_LOCK_ERROR);
        }

        Lock lock = new Lock(key,value,timeout);
        lock.start();
        return lock;
    }

    public void unlock(Lock lock) {
        stringRedisTemplate.execute(new DefaultRedisScript<>(UN_LOCK_SCRIPT, Long.class), Arrays.asList(lock.getKey()), lock.getValue());
        lock.stop();
    }
}