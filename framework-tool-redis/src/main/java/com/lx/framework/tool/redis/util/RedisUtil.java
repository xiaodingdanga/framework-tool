package com.lx.framework.tool.redis.util;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author xin.liu
 * @description Redis工具类封装
 * @date 2022-12-01  09:46
 * @Version 1.0
 */
@Slf4j
@Getter
@Component
public class RedisUtil {


    private static RedisTemplate<String, Object> redisTemplate;
    private static StringRedisTemplate stringRedisTemplate;


    @Autowired
    public void setRedisTemplate(@Qualifier("redisTemplate")  RedisTemplate<String, Object> redisTemplate) {
        log.debug("redisTemple加载");
        RedisUtil.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        log.debug("stringRedisTemplate加载");
        RedisUtil.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @description     向消息队列发送消息
     * @param key       消息队列名称
     * @param message   需要发送的消息
     * @return: org.springframework.data.redis.connection.stream.RecordId
     * @author xin.liu
     * @date 2022/12/9 15:26
     */
    public static RecordId addStream(String key, Map<String,Object> message){
        RecordId recordId = redisTemplate.opsForStream().add(key, message);
        return recordId;
    }

    /**
     * @description     添加消费者组
     * @param key       消息队列名称
     * @param groupName 消费者组名称
     * @return: void
     * @author xin.liu
     * @date 2022/12/9 15:23
     */
    public static void addGroup(String key, String groupName){
        redisTemplate.opsForStream().createGroup(key,groupName);
    }

    public static void addGroup(String key, String offerSet, String groupName){
        redisTemplate.opsForStream().createGroup(key, ReadOffset.from(offerSet),groupName);
    }

    /**
     * @description     消费者消息确认
     * @param key       消息队列名称
     * @param fieldId   消息ID
     * @return: void
     * @author xin.liu
     * @date 2022/12/9 15:23
     */
    public static void delField(String key, String fieldId){
        redisTemplate.opsForStream().delete(key,fieldId);
    }



    /**
     * @description 如果key在redis中不存在，则会自动初始化生成key，并返回0
     * @param key
     * @return: java.lang.Long
     * @author xin.liu
     * @date 2022/12/9 15:23
     */
    public static Long getIncr(String key){
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        redisAtomicLong.expire(61L, TimeUnit.DAYS);
        return redisAtomicLong.getAndIncrement();
    }


    /**
     * @description 自增
     * @param key 
     * @return: java.lang.Long
     * @author xin.liu
     * @date 2023/2/24 17:01
     */
    public static Long increment(String key){
        Long count = redisTemplate.opsForValue().increment(key, 1);
        return count;
    }




    /**
     * @description 指定缓存失效时间
     * @param key
     * @param time
     * @return: boolean
     * @author xin.liu
     * @date 2022/12/9 15:23
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */

    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("判断key是否存在,异常:{}", e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public static boolean del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return redisTemplate.delete(key[0]);
            } else {
                return redisTemplate.delete(CollUtil.newArrayList(key))==key.length;
            }
        }
        return false;
    }


    /**
     * 添分布式加锁  还可以增加重试逻辑 参数增加重试次数和重试间隔即可
     * @param lockKey key
     * @param lockID value
     * @param time 时间
     * @return
     */
    public static boolean lock(String lockKey,String lockID, long time) {
        log.info("添分布式加锁入参=====lockKey:"+lockKey+"=====lockID:"+lockID);
        try {
            return  redisTemplate.opsForValue().setIfAbsent(lockKey, lockID, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(lockKey+":添分布式加锁失败"+e.getMessage());
            return false;
        }
    }


    /**
     * 释放锁
     * @param lockKey
     * @param lockID
     */
    public static void unlock(String lockKey,String lockID) {
        log.info("释放锁入参=====lockKey:"+lockKey+"=====lockID:"+lockID);
        try {
            String value = (String) redisTemplate.opsForValue().get(lockKey);
            // 判断是否为当前线程加的锁
            if (Objects.equals(value, lockID)) {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(lockKey+":释放锁失败"+e.getMessage());
        }
    }

    /**
     * 模糊搜索所有匹配的key   key::*   慎用
     * @param key
     * @return
     */
    public static Set<String> keys(String key){
        return redisTemplate.keys(key);
    }

    /**
     * 删除
     * @param keys
     */
    public static void deleteKeys(Set<String> keys){
        redisTemplate.delete(keys);
    }

    /**
     * 模糊匹配删除
     * @param key
     */
    public static void deleteKeys(String key){
        Set<String> delete = keys(key);
        if (CollUtil.isNotEmpty(delete)) {
            redisTemplate.delete(delete);
        }
    }


    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> getMap(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean setMap(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于)
     * @return
     */
    public static double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于)
     * @return
     */
    public static double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 往set中增加元素
     *
     * @param key   Redis键
     * @param items set元素
     * @return 元素个数
     */
    public static Long sadd(String key, String... items) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        return setOperations.add(key, items);
    }

    /**
     * 从set中删除元素
     *
     * @param key Redis键
     * @param items set元素
     * @return 元素个数
     */
    public static Long srem(String key, Object... items) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        return setOperations.remove(key, items);
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public static Set<String> sget(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 从set中弹出元素
     *
     * @param key Redis键
     * @return 对象列表
     */
    public static String spop(String key) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        return setOperations.pop(key);
    }

    /**
     * 获取set的元素个数
     *
     * @param key Redis键
     * @return 对象列表
     */
    public static Long scard(String key) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        return setOperations.size(key);
    }

}
