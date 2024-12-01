package com.lx.framework.tool.startup.interceptor;

import cn.hutool.core.util.StrUtil;
import com.lx.framework.tool.utils.constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collections;

/**
 * @author xin.liu
 * @description 令牌中继:  RestTemplate 拦截器
 * @date 2023-02-08  11:37
 * @Version 1.0
 */
public class RestTemplateInterceptor {


    /**
     * @description 创建拦截器,添加请求头
     * @return: org.springframework.http.client.ClientHttpRequestInterceptor
     * @author xin.liu
     * @date 2023/2/8 11:38
     */
    public ClientHttpRequestInterceptor restTemplateInterceptor(){
        return new ClientHttpRequestInterceptor(){
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution) throws IOException {
                // 通过RequestContextHolder工具来获取请求相关变量
                ServletRequestAttributes attributes
                        = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(attributes != null) {
                    // 获取请求对象
                    HttpServletRequest servletRequest = attributes.getRequest();
                    // 优先获取请求头中的token
                    String token = StrUtil.isNotBlank(servletRequest.getHeader(Constants.AUTHORIZATION)) ?
                                                      servletRequest.getHeader(Constants.AUTHORIZATION):
                                                     (String) servletRequest.getAttribute(Constants.AUTHORIZATION);
                    if(StrUtil.isNotEmpty(token)) {
                        // 通过请求获取请求中的header参数
                        HttpHeaders headers = request.getHeaders();
                        // 往header中设置自己需要的参数
                        headers.add(Constants.AUTHORIZATION, token);
                    }
                }
                ClientHttpResponse response = execution.execute(request, body);
                return response;
            }
        };
    }


    /**
     * @description 添加指定的拦截器
     * @return: org.springframework.web.client.RestTemplate
     * @author xin.liu
     * @date 2023/2/8 11:38
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(restTemplateInterceptor()));
        return restTemplate;
    }
}
