package com.lx.framework.tool.startup.controller;

import cn.hutool.core.util.StrUtil;
import com.lx.framework.tool.startup.service.TokenService;
import com.lx.framework.tool.utils.base.Result;
import jakarta.annotation.Resource;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Description: 获取幂等token
 * Date: 2022/4/20 16:32
 * @author  xin.liu
 * @version 1.0.0
 */
//@Tag(name = "幂等性接口",description ="获取幂等token" )
@RestController
@RequestMapping("/idempotent")
public class IdempotentController {

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    private int num = 20;



    /**
     * Description 获取幂等性token
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 16:34
     */
//    @Operation(description = "获取幂等性token")
    @PostMapping("/getToken")
    public Result<String> getToken() {
        String token = tokenService.createToken();
        if (StrUtil.isNotEmpty(token)) {
            return Result.success(token);
        }
        return Result.success();
    }





    /**
     * Description 测试redis分布式锁(有锁)
     * @return void
     * @author xin.liu
     * @date 2022/4/20 16:33
     */
    @GetMapping("testLock")
    public void testLock() throws InterruptedException {
        // 获取锁
        Lock lock = redisLockRegistry.obtain("lock");
        // 尝试加锁
        boolean isLock = lock.tryLock(1, TimeUnit.SECONDS);
        String s = Thread.currentThread().getName();
        if (num > 0 && isLock) {
            System.out.println(s + ":排号成功，号码是：" + num);
            num--;
        } else {
            System.out.println(s + ":排号失败,号码已经被抢光");
        }
        // 释放锁
        lock.unlock();
    }
}