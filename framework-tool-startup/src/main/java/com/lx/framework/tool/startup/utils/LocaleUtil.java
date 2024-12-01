package com.lx.framework.tool.startup.utils;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-14  09:24
 * @Version 1.0
 */
@Component
public class LocaleUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    /**
     * @description     获取国际化信息
     * @param key       传入的国际化key
     * @param obj       传入的国际化参数
     * @param language  语言环境  zh-CN  en-US
     * @return: java.lang.String    返回国际化信息
     * @author xin.liu
     * @date 2024/3/14 9:48
     */
    public static String getI18n(String key, Object[] obj,String language) {
        String[] message=language.split("-");
        Locale locale =Locale.of(message[0],message[1]);
        if(ObjectUtil.isEmpty(locale)){
            locale=Locale.CHINA;
        }
        return applicationContext.getMessage(key, obj, key, locale);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (LocaleUtil.applicationContext == null) {
            LocaleUtil.applicationContext = applicationContext;
        }
    }

}
