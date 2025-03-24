package com.lx.framework.tool.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author xin.liu
 * @description Redis序列化配置
 * @date 2022-12-01  09:59
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * @description RedisTemplate redis工具类配置
     * @param redisConnectionFactory
     * @return: org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object>
     * @author xin.liu
     * @date 2022/12/1 10:33
    */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.debug("redisTemplate实例化");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
        // key的序列化采用StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value值的序列化采用fastJsonRedisSerializer
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



//    /**
//     * @description StringRedisTemplate redis工具类配置
//     * @param redisConnectionFactory
//     * @return: org.springframework.data.redis.core.StringRedisTemplate
//     * @author xin.liu
//     * @date 2022/12/1 10:34
//    */
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        log.debug("stringRedisTemplate实例化");
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
//        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(String.class);
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // Json序列化配置
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        // key的序列化采用StringRedisSerializer
//        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
//        // value值的序列化采用fastJsonRedisSerializer
//        stringRedisTemplate.setValueSerializer(fastJsonRedisSerializer);
//        // hash的key也采用String的序列化方式
//        stringRedisTemplate.setHashKeySerializer(stringRedisSerializer);
//        // hash的value序列化方式采用jackson
//        stringRedisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
//        stringRedisTemplate.afterPropertiesSet();
//        return stringRedisTemplate;
//    }
//
//    /**
//     * @description ReactiveRedisTemplate redis工具类配置  面对大数据量操作使用
//     * @param connectionFactory
//     * @return: org.springframework.data.redis.core.ReactiveRedisTemplate<java.lang.String,java.lang.Object>
//     * @author xin.liu
//     * @date  16:22
//     */
//    @Bean
//    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
//
//        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder =
//                RedisSerializationContext.newSerializationContext();
//        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        redisSerializer.setObjectMapper(om);
//
//        builder.key(new StringRedisSerializer());
//        builder.value(redisSerializer);
//        builder.hashKey(new StringRedisSerializer());
//        builder.hashValue(redisSerializer);
//        return new ReactiveRedisTemplate<>(connectionFactory,builder.build());
//    }

//    /**
//     * @description Redis 实现的分布式锁
//     * @param redisConnectionFactory
//     * @return: org.springframework.integration.redis.util.RedisLockRegistry
//     * @author xin.liu
//     * @date 2022/12/1 10:34
//    */
//    @Bean
//    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
//        // 1、锁的密钥前缀：REDIS-LOCK
//        // 2、锁的默认过期时间：60秒
//        return new RedisLockRegistry(redisConnectionFactory, "REDIS-LOCK",60000L);
//    }
}