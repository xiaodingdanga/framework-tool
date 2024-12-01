package com.lx.framework.tool.startup.handler.customException;

import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xin.liu
 * @description TODO
 * @date 2024-03-09  17:00
 * @Version 1.0
 */
@Data
public class AsyncException extends RuntimeException {

    /**
     * 错误枚举
     */
    protected CodeEnum codeEnum;

    public AsyncException() {
        super();
    }

    public AsyncException(CodeEnum codeEnum) {
        super();
        this.codeEnum=codeEnum;
    }

    public AsyncException(String message) {
        super(message);
    }
}