package com.lx.framework.tool.startup.interceptor;

import cn.hutool.core.lang.UUID;
import com.lx.framework.tool.utils.base.ThreadLocalUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author xin.liu
 * @description 自动添加跟踪ID拦截器
 * TODO 根据业务需要，根据condition注解做开关
 * @date 2023-04-28  10:58
 * @Version 1.0
 */
@Slf4j
@Component
public class TraceIdInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId=UUID.fastUUID().toString();
        log.info("requestId:"+requestId);
        // 初始化 ThreadLocal 获取 requestId
        ThreadLocalUtils.init(requestId);
        // 让日志能获取requestId作为traceId追踪使用
        MDC.put("nimbus_trace_id",requestId);
        // 必须返回true,否则会被拦截一切请求
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtils.close();
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.close();
    }

}
