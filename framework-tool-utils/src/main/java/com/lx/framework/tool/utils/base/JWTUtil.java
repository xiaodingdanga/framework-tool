package com.lx.framework.tool.utils.base;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JWTUtil {

	/**
	 * 过期时间，单位毫秒--14天
	 */
	public static final long EXPIRE_TIME_APP = 604800 * 2 * 1000;

	/**
	 * @Fields EXPIRE_TIME_PC : 过期时间，单位毫秒 2个小时
	 */
	public static final long EXPIRE_TIME_PC = 1 * 1000;

	/**
	 * @Fields SECRET : token 密钥
	 */
	private static final String SECRET = "SECRET";
	/**
	 * @desc 业务扩展字段
	 */
	public static final String USER_ID = "userId";
	public static final String USER_NAME = "userName";
	public static final String PLATFORM_CODE = "platformCode";
	
	
	/***
	 * @MethodName getJWT
	 * @Description 根据token获取jwt 
	 * @param token
	 * @return JWT
	 * @Author guokemeng
	 * @date 2023年8月9日 下午5:42:03
	 */
	public static JWT getJWT(String token) {
		try {
			return JWT.of(token);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 校验jwt是否正确
	 * @return 是否正确
	 */
	public static boolean verify(JWT jwt) {
		try {
			return jwt.setKey(SECRET.getBytes()).verify();
		} catch (Exception e) {
			return false;
		}
	}
	

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的用户名
	 */
	public static Long getUniqueId(JWT jwt) {
		return Long.parseLong(jwt.getPayload(USER_ID).toString());
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的platformCode
	 */
	public static String getPlatformCode(JWT jwt) {
		return jwt.getPayload(PLATFORM_CODE) == null ? "" : jwt.getPayload(PLATFORM_CODE).toString();
	}
	
	/**
	 * 获得token中的信息无需secret解密也能获得
	 * 
	 * @return token中包含的userName
	 */
	public static String getUserName(JWT jwt) {
		return jwt.getPayload(USER_NAME) == null ? "" : jwt.getPayload(USER_NAME).toString();
	}

	/**
	 * 生成签名, EXPIRE_TIME后过期
	 * @return 加密的token
	 */
	public static String sign(Map<String, Object> param, long expireTime) {
		Date newDate = new Date(System.currentTimeMillis() + expireTime);

		DateTime now = DateTime.now();
		Map<String, Object> payload = new ConcurrentHashMap<>();
		// 签发时间
		payload.put(JWTPayload.ISSUED_AT, now);
		// 过期时间
		payload.put(JWTPayload.EXPIRES_AT, newDate);
		// 生效时间
		payload.put(JWTPayload.NOT_BEFORE, now);
		//业务自定义字段
		payload.put(USER_ID, param.get(USER_ID));
		payload.put(USER_NAME, param.get(USER_NAME));
		payload.put(PLATFORM_CODE, param.get(PLATFORM_CODE));

		String token = cn.hutool.jwt.JWTUtil.createToken(payload, SECRET.getBytes());
		return token;
	}

	/**
	 * 校验token是否过期  false未过期true过期
	 */
	public static boolean verifyExpires(JWT jwt) {
		// 校验时间字段
		try {
			JWTValidator.of(jwt).validateDate(DateUtil.date(), 0);
		} catch (ValidateException e) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws InterruptedException {
		HashMap<String, Object> map = new HashMap<>();
		map.put(USER_ID,"1");
		map.put(USER_NAME,"2");
		map.put(PLATFORM_CODE,"3");
		String sign = sign(map,EXPIRE_TIME_PC);
		System.out.println(sign);
		Thread.sleep(2000);
		JWT jwt = getJWT(sign);
		System.out.println(verify(jwt));
		System.out.println(getUniqueId(jwt));
		System.out.println(verifyExpires(jwt));
	}

}
