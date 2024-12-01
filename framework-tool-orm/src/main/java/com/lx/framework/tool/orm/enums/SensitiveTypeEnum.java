package com.lx.framework.tool.orm.enums;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import lombok.Getter;


/**
 * @author xin.liu
 * @description 脱敏类型枚举
 * @date 2024-04-03  15:34
 * @Version 1.0
 */
@Getter
public enum SensitiveTypeEnum {
    DEFAULT("default", "默认") {
        @Override
        public String maskSensitiveData(String data) {
            // 默认原值返回，其他这个也没啥意义^_^
            return data;
        }
    },
    MOBILE("mobile", "手机号") {
        @Override
        public String maskSensitiveData(String data) {
            // 手机号前3位后4位脱敏，中间部分加*处理，比如：138****5678
            return DesensitizedUtil.mobilePhone(data);
        }
    },
    IDENTIFY("identify", "身份证号") {
        @Override
        public String maskSensitiveData(String data) {
            // 身份证前3位后4位脱敏，中间部分加*处理，比如：110***********3706
            return DesensitizedUtil.idCardNum(data, 3, 4);
        }
    },
    BANKCARD("bankcard", "银行卡号") {
        @Override
        public String maskSensitiveData(String data) {
            // 银行卡号前4位后4位脱敏，中间部分加*处理，比如：6225 **** **** *** 0845
            return DesensitizedUtil.bankCard(data);
        }
    },

    EMAIL("email", "邮箱") {
        @Override
        public String maskSensitiveData(String data) {
            // 邮箱@符号后明文显示，@符号前的字符串，只显示第一个字符，其余加*处理，比如：z***********@test.com
            return DesensitizedUtil.email(data);
        }
    },
    CHINESE_NAME("chinese_name", "中文姓名") {
        @Override
        public String maskSensitiveData(String data) {
            // 中文姓名脱敏
            return CharSequenceUtil.hide(String.valueOf(data), 1, data.length());
        }
    };


    /**
     * 脱敏类型
     */
    private String type;
    /**
     * 描述
     */
    private String desc;

    SensitiveTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * @description 遮挡敏感数据
     * @param data
     * @return: java.lang.String
     * @author xin.liu
     * @date 2024/4/3 15:36
     */
    public String maskSensitiveData(String data) {
        return data;
    }
}