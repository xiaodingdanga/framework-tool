package com.lx.framework.tool.utils.base;


import com.alibaba.fastjson2.JSON;
import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Description: 用于封装接口统一响应结果
 * Date: 2022/4/20 9:24
 * @author xin.liu
 * @version 1.0.0
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     *状态码，遵循HTTP Code要求，200为正常返回
     */
    private Integer code;
    /*
     *状态值
     */
    private String state;
    /*
     *响应信息
     */
    private String message;
    /*
     *响应中的数据
     */
    private T data;
    /*
     *单次请求唯一标识
     */
    private String requestId;



    /**
     * 响应数据
     */
    public static <T> Result<T> data(T data) {
        return new Result<T>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getState(), CodeEnum.SUCCESS.getMessage(), data, ThreadLocalUtils.get());
    }

    /**
     * 成功默认响应
     */
    public static <T> Result<T> success() {
        return new Result<T>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getState(), CodeEnum.SUCCESS.getMessage(), null, ThreadLocalUtils.get());
    }

    /**
     * 成功响应数据+提示信息
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getState(), message, data, ThreadLocalUtils.get());
    }

    /**
     * 成功响应提示信息
     */
    public static <T> Result<T> success(String message) {
        return new Result<T>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getState(), message, null, ThreadLocalUtils.get());
    }

    /**
     * 失败响应提示信息
     */
    public static <T> Result<T> error(String message) {
        log.debug("返回错误：code={}, message={}", CodeEnum.ERROR.getCode(), message);
        return new Result<T>(CodeEnum.ERROR.getCode(),CodeEnum.ERROR.getState(), message, null, ThreadLocalUtils.get());
    }

    /**
     * 失败响应提示信息+数据
     */
    public static <T> Result<T> error(String message,T data) {
        log.debug("返回错误：code={}, message={}", CodeEnum.ERROR.getCode(), message);
        return new Result<T>(CodeEnum.ERROR.getCode(),CodeEnum.ERROR.getState(), message, data, ThreadLocalUtils.get());
    }

    /**
     * 自定义构建响应码+提示信息
     */
    public static <T> Result<T> build(int code, String message) {
        log.debug("返回结果：code={}, message={}", code, message);
        return new Result<T>(code,null, message, null, ThreadLocalUtils.get());
    }

    /**
     * 使用枚举自定义构建响应码+提示信息
     */
    public static <T> Result<T> build(CodeEnum codeEnum) {
        log.debug("返回结果：code={}, message={}", codeEnum.getCode(), codeEnum.getMessage());
        return new Result<T>(codeEnum.getCode(),codeEnum.getState(), codeEnum.getMessage(), null, ThreadLocalUtils.get());
    }


    /**
     * 格式化为json
     */
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}