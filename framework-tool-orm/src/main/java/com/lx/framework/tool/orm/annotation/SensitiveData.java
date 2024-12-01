package com.lx.framework.tool.orm.annotation;

import java.lang.annotation.*;

/**
 * 该注解定义在类上
 * 插件通过扫描类对象是否包含这个注解来决定是否继续扫描其中的字段注解
 * 这个注解要配合SensitiveField注解
 * @author xin.liu
 * @date 2024/4/3 15:32
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {

}
