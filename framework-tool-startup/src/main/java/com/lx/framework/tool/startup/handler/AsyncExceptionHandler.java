package com.lx.framework.tool.startup.handler;

import com.alibaba.fastjson2.JSON;
import com.lx.framework.tool.startup.handler.customException.AsyncException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @author xin.liu
 * @description 如果你的方法没有返回类型又想处理未捕获异常异常的话，用它就是了。
 * @date 2024-03-09  16:58
 * @Version 1.0
 */
@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.info("---Async method: {} has uncaught exception,params:{}", method.getName(), JSON.toJSONString(params));

        if (ex instanceof AsyncException) {
            AsyncException asyncException = (AsyncException) ex;
            log.info("---asyncException:{}", asyncException.getCodeEnum().getMessage());
        }
        log.info("---Exception :");
        ex.printStackTrace();
    }
}