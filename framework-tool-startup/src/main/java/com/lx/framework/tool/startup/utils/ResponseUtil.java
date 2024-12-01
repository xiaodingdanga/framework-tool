package com.lx.framework.tool.startup.utils;


import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xin.liu
 * @description 响应工具类
 * @date 2022-12-13  18:05
 * @Version 1.0
 */
public class ResponseUtil {



    /**
     * @description 返回的json值
     * @param response
     * @param json
     * @return: void
     * @author xin.liu
     * @date 2022/12/1 10:49
     */
    public static void writeReturnJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null){
                writer.close();
            }
        }
    }
}
