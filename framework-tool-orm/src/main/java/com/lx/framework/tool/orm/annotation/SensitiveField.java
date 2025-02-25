package com.lx.framework.tool.orm.annotation;



import com.lx.framework.tool.orm.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * 该注解有两种使用方式
 * ①：配合@SensitiveData加在类中的字段上
 * ②：直接在Mapper中的方法参数上使用
 * @author xin.liu
 * @date 2024/4/3 15:33
 */
@Documented
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {

    /**
     * 定义加解密类型
     */
//    SensitiveTypeEnum type() default SensitiveTypeEnum.DEFAULT;

    /**
     * 定义脱敏类型
     */
    SensitiveTypeEnum type() default SensitiveTypeEnum.DEFAULT;
}

