package com.lx.framework.tool.startup.interceptor;

import cn.hutool.json.JSONUtil;
import com.lx.framework.tool.redis.annotation.AutoIdempotent;
import com.lx.framework.tool.startup.service.TokenService;
import com.lx.framework.tool.utils.base.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Description: 幂等性注解 @AutoIdempotent 拦截器
 * Date: 2022/4/20 16:25
 * @author: xin.liu
 * @version 1.0.0
 */
@Component
public class AutoIdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;


    /**
     * Description 预处理
     * @param request
     * @param response
     * @param handler
     * @return boolean
     * @author xin.liu
     * @date 2022/4/20 16:26
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 被ApiIdempotment标记的扫描
        AutoIdempotent methodAnnotation = method.getAnnotation(AutoIdempotent.class);
        if (methodAnnotation != null) {
            try {
                // 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
                return tokenService.checkToken(request);
            } catch (Exception ex) {
                writeReturnJson(response, JSONUtil.toJsonStr(Result.error(ex.getMessage())));
                throw ex;
            }
        }
        // 必须返回true,否则会被拦截一切请求
        return true;
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }



    /**
     * Description 返回的json值
     * @param response
     * @param json
     * @return void
     * @author xin.liu
     * @date 2022/4/20 16:26
     */
    private void writeReturnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }
}