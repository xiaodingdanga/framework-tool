package com.lx.framework.tool.redis.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DistributedLockUtil {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取分布式锁
     *
     * @param lockName 锁的名称
     * @param waitTime 最多等待时间
     * @param leaseTime 锁的自动释放时间
     * @param unit 时间单位
     * @return 是否获取到锁
     */
    public boolean tryLock(String lockName, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockName 锁的名称
     */
    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
