
package com.lx.framework.tool.startup.config;




import com.lx.framework.tool.startup.interceptor.AutoIdempotentInterceptor;
import com.lx.framework.tool.startup.interceptor.TraceIdInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Description:
 * Date: 2022/4/20 16:28
 * @author: xin.liu
 * @version 1.0.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;

    @Resource
    private TraceIdInterceptor traceIdInterceptor;

    /**
     * Description 添加拦截器
     * @param registry
     * @author xin.liu
     * @date 2022/4/20 16:31
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autoIdempotentInterceptor);
        registry.addInterceptor(traceIdInterceptor);
    }
}
