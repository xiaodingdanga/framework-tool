package com.lx.framework.tool.startup.utils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * Description: 日志工具类
 * Date: 2022/4/27 16:07
 * @author: xin.liu
 * @version 1.0.0
 */
@UtilityClass
@Slf4j
public class LogUtil {


    /**
     * Description 根据属性获取对应的值
     * @param fieldName
     * @param o
     * @return java.lang.Object
     * @author xin.liu
     * @date 2022/4/27 16:08
     */
    public  Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            log.warn("获取属性值失败！" + e);
        }
        return null;
    }


    /**
     * Description 获取堆栈信息
     * @param throwable
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/27 16:09
     */
    public String getStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        }
    }


    /**
     * Description 获取IP
     * @param request
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/28 10:05
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * Description IP检测
     * @param ip
     * @return boolean
     * @author xin.liu
     * @date 2022/4/28 10:04
     */
    private static boolean checkIP(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)
                || ip.split(".").length != 4) {
            return false;
        }
        return true;
    }
}
