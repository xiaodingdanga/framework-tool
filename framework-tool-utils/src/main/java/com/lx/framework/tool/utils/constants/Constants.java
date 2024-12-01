package com.lx.framework.tool.utils.constants;

/**
 * Description:
 * Date: 2022/5/16 17:42
 * @author: xin.liu
 * @version 1.0.0
 */
public abstract class Constants {

    //必须全部大写，单词间下划线分隔

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** TOP Date默认时区 **/
    public static final String DATE_TIMEZONE = "GMT+8";

    /** UTF-8字符集 **/
    public static final String CHARSET_UTF8 = "UTF-8";

    /** GBK字符集 **/
    public static final String CHARSET_GBK = "GBK";

    public static final String AUTHORIZATION="Authorization";

    /** 用户会话信息 **/
    public static final String USER_SESSION = "USER_SESSION";

    /** 超级管理员 **/
    public static final String SUPER_ADMIN = "super_admin";

    /** 管理员 **/
    public static final String ADMIN = "admin";

    /** 超级管理员id **/
    public static final Integer SUPER_ADMIN_ID = 1;

    /** TOP JSON 应格式 */
    public static final String FORMAT_JSON = "json";

    /** TOP XML 应格式 */
    public static final String FORMAT_XML = "xml";

    /** 签名方法 */
    public static final String SIGN_KEY = "sign";

    /** 签名方法 */
    public static final String SIGN_METHOD = "signMethod";

    /** MD5签名方式 */
    public static final String SIGN_METHOD_MD5 = "md5";

    /** HMAC签名方式 */
    public static final String SIGN_METHOD_HMAC = "hmac";

    /** APP_SECRET */
    public static final String APP_SECRET = "APP_SECRET";

    /** API_FREQUENCY */
    public static final String API_FREQUENCY = "API_FREQUENCY";

    /** APP_FREQUENCY */
    public static final String APP_FREQUENCY = "APP_FREQUENCY";

    /** PRIVATE_KEY */
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    /** PUBLIC_KEY */
    public static final String PUBLIC_KEY = "PUBLIC_KEY";

    /** DOMAIN */
    public static final String DOMAIN = "DOMAIN";

    /** METHOD */
    public static final String METHOD = "METHOD";

    /**
     * Redis中存储的动态路由版本号Key
     */
    public static final String DYNAMIC_ROUTE_VERSION = "dynamic-route-version";

    /**
     * Redis中存储的动态路由Key
     */
    public static final String DYNAMIC_ROUTE_SERVER_NAME = "dynamic-route-service";



}
