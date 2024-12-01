package com.lx.framework.tool.redis.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author xin.liu
 * @description 序列化
 * @date 2022-5-30  09:21
 * @Version 1.0
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }


    /**
     * @description Redis 序列化
     * @param t
     * @return: byte[]
     * @author xin.liu
     * @date 2023/5/30 9:52
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        if(t instanceof String){
            return ((String) t).getBytes(DEFAULT_CHARSET);
        }
        return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
    }


    /**
     * @description Redis 反序列化
     * @param bytes
     * @return: T
     * @author xin.liu
     * @date 2023/5/30 9:49
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        if (JSON.isValid(str)) {
            try {
                Object object = JSON.parse(str);
                if (object instanceof JSONObject || object instanceof JSONArray) {
                    return JSON.parseObject(str, clazz);
                } else {
                    return (T) str;
                }
            } catch (Exception e) {
                return (T) str;
            }
        } else {
            return (T) str;
        }
    }
}
