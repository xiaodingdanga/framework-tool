package com.lx.framework.tool.utils.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xin.liu
 * @description ThreadLocal 工具类
 * @date 2023-04-25  11:08
 * @Version 1.0
 */
public class ThreadLocalUtils {

    /**
     * 当前线程的本地存储
     */
    private static ThreadLocal<String> threadLocal = new ThreadLocal();

    private static ThreadLocal<ConcurrentHashMap<String,Object>> mapThreadLocal = new ThreadLocal();

    /**
     * 线程初始化设置值
     */
    public static void init(String requestId){
        threadLocal.set(requestId);
    }

    /**
     * 从当前线程获取请求Id
     */
    public static String get(){
        return threadLocal.get();
    }

    /**
     * 清空线程本地存储 防止OOM
     */
    public static void close(){
        threadLocal.remove();
    }

    
    public static void initMap(){
        mapThreadLocal.set(new ConcurrentHashMap());
    }
    
    public static void setValue(String key,Object value){
        ConcurrentHashMap<String,Object> map = mapThreadLocal.get();
        map.put(key,value);
    }
    
    public static Object getValue(String key){
        ConcurrentHashMap<String,Object> map = mapThreadLocal.get();
        return map.get(key);
    }
    public static void closeMap(){
        mapThreadLocal.remove();
    }
}
