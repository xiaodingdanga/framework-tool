package com.lx.framework.tool.startup.handler.customException;


import com.lx.framework.tool.utils.enums.CodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description: 自定义参数异常
 * Date: 2022/4/20 14:31
 * @author  xin.liu
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class ParamException extends RuntimeException {
    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;


    public ParamException() {
        super();
        this.errorCode = CodeEnum.ERROR.getCode();
        this.errorMsg = "参数错误";
    }

    public ParamException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public ParamException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

}