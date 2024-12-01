package com.lx.framework.tool.startup.handler.customException;


import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.Data;

/**
 * Description:
 * Date: 2022/4/20 14:31
 * @author  xin.liu
 * @version 1.0.0
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * 错误枚举
     */
    protected CodeEnum codeEnum;

    public BusinessException() {
        super();
    }

    public BusinessException(CodeEnum codeEnum) {
        super();
        this.codeEnum=codeEnum;
    }

    public BusinessException(String message) {
        super(message);
    }
}
