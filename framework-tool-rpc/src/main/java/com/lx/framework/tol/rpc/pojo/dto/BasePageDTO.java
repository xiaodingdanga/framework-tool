package com.lx.framework.tol.rpc.pojo.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author xin.liu
 * @description TODO
 * @date 2023-05-18  09:51
 * @Version 1.0
 */
@Data
//@Schema(description = "BasePageDTO对象")
public class BasePageDTO {

//    @Schema(description = "当前页",requiredMode = Schema.RequiredMode.REQUIRED,defaultValue = "1")
    @JSONField(serialize = false)
    @NotNull(message = "当前页不能为空")
    private Integer page = 1;


//    @Schema(description = "每页多少条",requiredMode = Schema.RequiredMode.REQUIRED,defaultValue = "15")
    @JSONField(serialize = false)
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize = 15;

}
