package com.lx.framework.tool.orm.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.lx.framework.tool.orm.annotation.SensitiveField;
import com.lx.framework.tool.orm.enums.SensitiveTypeEnum;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author xin.liu
 * @description 固定加解密工具类
 * @date 2024-04-03  15:19
 * @Version 1.0
 */
public class EncryptionUtil {
    /**
     * 加密标识：字符串有这个前缀就说明已经加密过
     */
    public static final String KEY_SENSITIVE = "SENSITIVE_";

    private static final byte[] KEYS = "9ee5543ce7bd002aaf9148613f228f8c".getBytes(StandardCharsets.UTF_8);

    private static AES aes = SecureUtil.aes(KEYS);


    /**
     * @description 使用固定key进行加密
     * @param parameter
     * @return: java.lang.String
     * @author xin.liu
     * @date 2024/4/3 15:31
     */
    public static String encrypt(String parameter){
        if(parameter.startsWith(KEY_SENSITIVE)){
            return parameter;
        }
        return KEY_SENSITIVE+aes.encryptHex(parameter);
    }


    /**
     * @description 使用固定key进行解密
     * @param parameter
     * @return: java.lang.String
     * @author xin.liu
     * @date 2024/4/3 15:30
     */
    public static String decrypt(String parameter){
        if(!parameter.startsWith(KEY_SENSITIVE)){
            return parameter;
        }
        parameter=parameter.substring(10);
        return aes.decryptStr(parameter);
    }

    /**
     * @description 通过反射，获取需要解密、脱敏的字段，进行脱敏、解密
     * @param result
     * @return: T
     * @author xin.liu
     * @date 2024/4/3 15:38
     */
    public static <T> T decrypt(T result) throws IllegalAccessException {
        // 取出resultType的类
        Class<?> resultClass = result.getClass();
        Field[] declaredFields = resultClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 取出所有被DecryptTransaction注解的字段
            SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
            if (!Objects.isNull(sensitiveField)) {
                field.setAccessible(true);
                Object object = field.get(result);
                //String的解密
                if (object instanceof String) {
                    String value = (String) object;
                    // 对注解的字段进行逐一解密
                    value= EncryptionUtil.decrypt(value);
                    // 脱敏
                    SensitiveTypeEnum type = sensitiveField.type();
                    value = type.maskSensitiveData(value);
                    field.set(result, value);
                }
            }
        }
        return result;
    }



    /**
     * @description 通过反射，获取需要加密的字段，进行加密
     * @param declaredFields
     * @param paramsObject
     * @return: T
     * @author xin.liu
     * @date 2024/4/3 15:39
     */
    public static <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
        //取出所有被EncryptTransaction注解的字段
        for (Field field : declaredFields) {
            SensitiveField sensitiveField = field.getAnnotation(SensitiveField.class);
            if (!Objects.isNull(sensitiveField)) {
                //当isAccessible()的结果是false时不允许通过反射访问该字段
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                // 暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    //开始对字段加密使用自定义的AES加密工具
                    field.set(paramsObject, StrUtil.isNotBlank(value)?EncryptionUtil.encrypt(value):value);
                }
            }
        }
        return paramsObject;
    }
}
