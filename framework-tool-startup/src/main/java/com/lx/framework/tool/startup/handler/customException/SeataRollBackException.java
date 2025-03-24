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
public class SeataRollBackException extends RuntimeException {

    /**
     * 错误枚举
     */
    protected CodeEnum codeEnum;

    public SeataRollBackException() {
        super();
    }

    public SeataRollBackException(CodeEnum codeEnum) {
        super();
        this.codeEnum=codeEnum;
    }

    public SeataRollBackException(String message) {
        super(message);
    }
}
