package com.lx.framework.tool.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xin.liu
 * @description 状态码枚举
 * @date 2023-04-28  14:36
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {

    /**
     * 定义状态码及信息
     */
    SUCCESS(200, "success","业务成功"),
    ERROR(999, "error","业务失败"),
    UNAUTHORIZED(401, "Unauthorized","会话未能通过登录认证"),
    RESOURCE_NOT_EXISTS(404, "resource_not_exists","资源不存在"),
    PERMISSION_DENIED(403, "permission_denied","访问不允许"),
    DELETE_FAILURE(500, "delete_failure","删除失败"),
    ASYNC_ERROR(500, "async_error","异步线程任务异常"),
    REDIS_LOCK_ERROR(500, "redis_lock_error","获取redis分布式锁失败"),
    SYSTEM_ERROR(500, "system_error","系统未知异常");

    private int code;

    private String state;

    private String message;
}
