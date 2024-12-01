package com.lx.framework.tool.startup.controller;


import com.lx.framework.tool.utils.base.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * Description: 统一错误处理
 * Date: 2023/5/30 14:12
 * @author: xin.liu
 * @version 1.0.0
 */
@Slf4j
@RestController
public class HttpErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @ResponseBody
    @RequestMapping(path  = ERROR_PATH )
    public Result error(HttpServletRequest request, HttpServletResponse response){
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values());
        Map<String, Object> map=this.errorAttributes.getErrorAttributes(servletWebRequest, errorAttributeOptions);
        return Result.build((Integer) map.get("status"),map.get("message").toString());
    }
}
