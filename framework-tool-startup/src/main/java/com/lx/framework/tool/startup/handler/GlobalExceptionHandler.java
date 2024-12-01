package com.lx.framework.tool.startup.handler;


import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.lx.framework.tool.startup.handler.customException.AsyncException;
import com.lx.framework.tool.startup.handler.customException.BusinessException;
import com.lx.framework.tool.startup.handler.customException.ParamException;
import com.lx.framework.tool.startup.handler.customException.RedisLockException;
import com.lx.framework.tool.startup.utils.LocaleUtil;
import com.lx.framework.tool.startup.utils.RequestUtil;
import com.lx.framework.tool.utils.base.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Description: 全局异常处理类
 * Date: 2023/5/30 14:29
 * @author  xin.liu
 * @version 1.0.0
 */

@Slf4j
@RestControllerAdvice  //等于@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {

    /**
     * Description 获取异常类型
     * @param e
     * @return java.lang.Throwable
     * @author xin.liu
     * @date 2022/4/20 14:35
     */
    public static Throwable getExceptionType(Exception e) {
        e.printStackTrace();
        return e;
    }

    /**
     * Description 获取异常信息
     * @param e
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/20 14:35
     */
    public static String getExceptionMessage(Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }

    /**
     * Description 获取堆栈信息
     * @param e
     * @return java.lang.String
     * @author xin.liu
     * @date 2022/4/20 14:35
     */
    public static String getExceptionStringStackTrace(Exception e) {
        e.printStackTrace();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Description 处理系统内部异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 14:35
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e){
        System.out.println("languages:"+ RequestUtil.getLanguage());
        String error= LocaleUtil.getI18n("global.error", null,RequestUtil.getLanguage());
        System.out.println("error:"+error);
        // 未知异常
        log.error("全局异常！未知异常：{}", getExceptionType(e)+":"+e.getMessage());
        // 给前端页面友好的提示
        return Result.error(error);
    }

    /**
     * Description 基于 @Validated 对body参数校验异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/25 20:18
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> validExceptionHandler(MethodArgumentNotValidException  e){
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        // 封装异常信息
        for (FieldError error : result.getFieldErrors()) {
            String field = error.getField();
            String msg = error.getDefaultMessage();
            String message = String.format("%s:%s ", field, msg);
            sb.append(message);
        }
        return Result.error(sb.toString());
    }

    /**
     * Description  基于 @Validated 对query参数校验异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/27 14:04
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> validExceptionHandler(ConstraintViolationException e){
        StringBuffer sb = new StringBuffer();
        for(ConstraintViolation constraint:e.getConstraintViolations()){
            sb.append(constraint.getMessageTemplate());
        }
        return Result.error(sb.toString());
    }

    /**
     * Description 处理自定义的参数异常异常
     * @param req
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 14:36
     */
    @ExceptionHandler(ParamException.class)
    public Result paramExceptionHandler(HttpServletRequest req, ParamException e){
        e.printStackTrace();
        log.error("参数异常！原因是：{}",e.getErrorMsg());
        return Result.build(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * Description 处理自定义异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 14:36
     */
    @ExceptionHandler(AsyncException.class)
    public Result<String> handleAsyncException(AsyncException e) {
        log.error("线程池异步任务执行异常:", e);
        if(ObjectUtil.isNotEmpty(e.getCodeEnum())){
            return Result.build(e.getCodeEnum().getCode(),e.getCodeEnum().getMessage());
        }
        return Result.error(e.getMessage());
    }

    /**
     * @description 测试分布式锁异常拦截
     * @param e
     * @return: com.lx.framework.tool.utils.base.Result<java.lang.String>
     * @author xin.liu
     * @date  13:50
     */
    @ExceptionHandler(RedisLockException.class)
    public Result<String> handleRedisLockException(RedisLockException e) {
        log.error("redis分布式锁获取失败:", e);
        if(ObjectUtil.isNotEmpty(e.getCodeEnum())){
            return Result.build(e.getCodeEnum().getCode(),e.getCodeEnum().getMessage());
        }
        return Result.error(e.getMessage());
    }

    /**
     * Description 处理自定义异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 14:36
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        if(ObjectUtil.isNotEmpty(e.getCodeEnum())){
            return Result.build(e.getCodeEnum());
        }
        return Result.error(e.getMessage());
    }

    /**
     * Description 数据库唯一约束异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/4/20 14:36
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(HttpServletRequest req,DuplicateKeyException e){
        String message=e.getCause().getMessage();
        String regexResult=ReUtil.getGroup0(" '.*' ", message);
        log.error("数据库唯一约束异常:", e);
        return Result.error(regexResult+":已经存在");
    }

    /**
     * Description 调试异常处理
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/6/18 11:40
     */
    @ExceptionHandler(IORuntimeException.class)
    public Result handleIORuntimeException(IORuntimeException e) {
        return Result.error(e.getMessage());
    }

    /**
     * Description 任务拒绝策略异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/7/28 18:29
     */
    @ExceptionHandler(TaskRejectedException.class)
    public Result handleTaskRejectedException(TaskRejectedException e) {
        log.error("系统流量到达瓶颈,触发任务拒绝策略：{}", getExceptionType(e)+":"+e.getMessage());
        return Result.error("系统繁忙，请稍后再试");
    }

    /**
     * Description 参数校验异常
     * @param e
     * @return com.qizhidao.open.api.util.base.Result
     * @author xin.liu
     * @date 2022/8/2 11:07
     */
    @ExceptionHandler(ValidationException.class)
    public Result handleValidationException(ValidationException e) {
        return Result.error(e.getMessage());
    }
}
