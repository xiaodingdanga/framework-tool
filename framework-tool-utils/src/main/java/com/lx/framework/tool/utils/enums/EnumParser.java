package com.lx.framework.tool.utils.enums;

/**
 * Description: 枚举类需实现EnumBase接口
 * Date: 2022/8/4 16:38
 * @author: xin.liu
 * @version 1.0.0
 */
public class EnumParser {

    /**
     * Description      根据枚举值获取枚举类型
     * @param typeClass 枚举类
     * @param value     枚举值
     * @return java.lang.Object
     * @author xin.liu
     * @date 2022/8/5 9:15
     */
    public static Object parse(Class<?> typeClass, Object value) {
        Class<? extends Enum> enumTmp = typeClass.asSubclass(Enum.class);
        Object[] constants = enumTmp.getEnumConstants();
        int size = constants.length;
        for (int i = 0; i < size; i++) {
            Object tmp = constants[i];
            String val = String.valueOf(((EnumBase) tmp).getValue());
            if (val.equals(String.valueOf(value))) {
                return tmp;
            }
        }
        return null;
    }
}
