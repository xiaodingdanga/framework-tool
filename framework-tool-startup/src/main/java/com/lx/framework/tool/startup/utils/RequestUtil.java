package com.lx.framework.tool.startup.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * @author xin.liu
 * @description 请求工具类
 * @date 2022-12-01  09:59
 * @Version 1.0
 */
public class RequestUtil {

    /**
     * @description 获取请求头中语言信息
     * @return: java.lang.String
     * @author xin.liu
     * @date 2024/3/14 16:21
     */
    public static String getLanguage() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String language = request.getHeader("Accept-Language");
        String[] languages=language.split(";");
        return languages[0];
    }

    /**
     * @description     获取请求类中传递过来的所有参数
     * @param request
     * @return: java.util.Map<java.lang.String,java.lang.String>
     * @author xin.liu
     * @date 2022/12/1 10:45
     */
    public static Map<String, String> getAllRequestParam(HttpServletRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder wholeStr = new StringBuilder();
        //一行一行的读取body体里面的内容；
        while ((str = reader.readLine()) != null) {
            wholeStr.append(str);
        }
        if(wholeStr.toString().startsWith("[")){
            Map<String, String> map = new HashMap<>();
            map.put("arrayString", wholeStr.toString());
            return map;
        }else {
            //转化成json对象
            return JSONObject.parseObject(wholeStr.toString(), Map.class, JSONReader.Feature.FieldBased);
        }
    }




    /**
     * @description 获取Request的body数据
     * @param request
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/11 14:36
     */
    public static JSONObject getBodyJSON(ServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.isValid(stringBuilder.toString())?JSONObject.parseObject(stringBuilder.toString()):new JSONObject();
    }


    /**
     * @description 获取请求体字符串
     * @param request
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/4/14 9:33
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int len;
                while ((len=bufferedReader.read(charBuffer))!=-1){
                    stringBuilder.append(charBuffer,0,len);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }
}
