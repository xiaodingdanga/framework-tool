package com.lx.framework.tool.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yanzhi.chen
 * @description 删除状态枚举
 * @date 2023-07-03  08:58
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum DeletedEnum implements EnumBase{
    UNDELETE(0,"非删除"),
    DELETED(1,"已删除");
    /**
     * 字段值
     */
    private Integer code;
    /**
     * 值描述
     */
    private String describe;

    @Override
    public Object getValue() {
        return this.code;
    }
}
