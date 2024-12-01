package com.lx.framework.tool.startup.handler.customException;

import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.Data;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-09  17:00
 * @Version 1.0
 */
@Data
public class RedisLockException extends RuntimeException {

    /**
     * 错误枚举
     */
    protected CodeEnum codeEnum;

    public RedisLockException() {
        super();
    }

    public RedisLockException(CodeEnum codeEnum) {
        super();
        this.codeEnum=codeEnum;
    }

    public RedisLockException(String message) {
        super(message);
    }
}