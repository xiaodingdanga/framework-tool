package com.lx.framework.tool.startup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author xin.liu
 * @description 国际化支持
 * @date 2024-03-14  09:19
 * @Version 1.0
 */
@Configuration
public class LocaleConfig {


    /**
     * @description 国际化信息配置
     * @return: org.springframework.context.support.ResourceBundleMessageSource
     * @author xin.liu
     * @date 2024/3/14 9:22
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        Locale.setDefault(Locale.CHINA);
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        //设置国际化文件存储路径和名称    i18n目录，messages文件名
        resourceBundleMessageSource.setBasenames("i18n/messages", "i18n/error", "i18n/message-system");
        //设置根据key如果没有获取到对应的文本信息,则返回key作为信息
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        //设置字符编码
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return resourceBundleMessageSource;
    }

}
