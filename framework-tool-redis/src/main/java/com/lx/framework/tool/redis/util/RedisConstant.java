package com.lx.framework.tool.redis.util;

/**
 * Description: Redis相关常量
 * Date: 2022/4/20 16:14
 * @author  xin.liu
 * @version 1.0.0
 */
public interface RedisConstant {

    /**
     * 默认的处理验证码的url前缀
     */
    public static final String TOKEN_PREFIX = "AutoIdempotent_";

    /**
     * 默认的处理验证码的url前缀
     */
    public static final String TOKEN_NAME= "Idempotent_Token";

}
