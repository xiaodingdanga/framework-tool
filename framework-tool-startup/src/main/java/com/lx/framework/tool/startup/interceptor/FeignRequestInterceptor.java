package com.lx.framework.tool.startup.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lx.framework.tool.utils.base.ThreadLocalUtils;
import com.lx.framework.tool.utils.constants.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * @author xin.liu
 * @description 令牌中继: 使用 Feign进行远程调用时，先经过此拦截器，在此拦截器中将请求头带上访问令牌
 * @date 2023-02-07  11:11
 * @Version 1.0
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {


    /**
     * @description 将请求头带上访问令牌
     * @param requestTemplate
     * @return: void
     * @author xin.liu
     * @date 2023/2/7 11:15
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 通过RequestContextHolder工具来获取请求相关变量
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
            // 获取请求对象
            HttpServletRequest request = attributes.getRequest();
            // 优先获取请求头中的token
            String token = StrUtil.isNotBlank(request.getHeader(Constants.AUTHORIZATION)) ?
                                              request.getHeader(Constants.AUTHORIZATION):
                                              (String) request.getAttribute(Constants.AUTHORIZATION);
            if(StringUtils.isNotEmpty(token)) {
                // Bearer xxx
                // 在使用feign远程调用时，请求头就会带上访问令牌
                requestTemplate.header(Constants.AUTHORIZATION, token);
            }
        }else {
            // 异步线程获取token
            String token = ThreadLocalUtils.get();
            if(StringUtils.isNotEmpty(token)) {
                // Bearer xxx
                // 在使用feign远程调用时，请求头就会带上访问令牌
                requestTemplate.header(Constants.AUTHORIZATION, token);
            }
            ThreadLocalUtils.close();
        }
    }
}
