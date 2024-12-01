package com.lx.framework;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-02-28  14:40
 * @Version 1.0
 */
@SpringBootApplication
public class SaTokenDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SaTokenDemoApplication.class, args);
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }
}