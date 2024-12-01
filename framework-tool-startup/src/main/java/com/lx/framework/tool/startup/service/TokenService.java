package com.lx.framework.tool.startup.service;


import jakarta.servlet.http.HttpServletRequest;

/**
 * Description: 基于redis实现接口幂等性  获取token
 * Date: 2022/4/20 16:09
 * @author  xin.liu
 * @version 1.0.0
 */
public interface TokenService {


    /**
     * Description 创建token
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/20 16:10
     */
    public String createToken();


    /**
     * Description 检查token
     * @param request
     * @return boolean
     * @author xin.liu
     * @date 2022/4/20 16:10
     */
    public boolean checkToken(HttpServletRequest request) throws Exception;
}
