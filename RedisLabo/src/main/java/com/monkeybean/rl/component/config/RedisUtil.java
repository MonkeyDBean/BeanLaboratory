package com.monkeybean.rl.component.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 多点部署时, 引入redis保证缓存一致性, CacheData类中的数据存储全部替换为Redis存储
 * <p>
 * Created by MonkeyBean on 2019/7/28.
 */
@Component
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    private static ValueOperations<String, Object> valueOperations;

    private static HashOperations<String, String, Object> hashOperations;

    @Autowired
    @SuppressWarnings("unchecked")
    public RedisUtil(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
        RedisUtil.valueOperations = RedisUtil.redisTemplate.opsForValue();
        RedisUtil.hashOperations = RedisUtil.redisTemplate.opsForHash();
    }

    /**
     * 获取指定值
     */
    public static Object getValue(String key) {
        return RedisUtil.valueOperations.get(key);
    }

    /**
     * 写入指定值
     *
     * @param expireTime 过期时间, 毫秒
     */
    @SuppressWarnings("unchecked")
    public static void setValue(String key, Object value, long expireTime) {
        if (RedisUtil.valueOperations.setIfAbsent(key, value)) {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 写入指定值
     */
    public static void setValue(String key, Object value) {
        RedisUtil.valueOperations.set(key, value);
    }

    /**
     * 获取指定hash表中的值
     *
     * @param hashName hash表名称
     * @param hashKey  hash表key
     * @return hash value, 若不存在返回null
     */
    public static Object getHashValue(String hashName, String hashKey) {
        return RedisUtil.hashOperations.get(hashName, hashKey);
    }

    /**
     * 写入指定的hash值
     *
     * @param hashName  hash表名称
     * @param hashKey   hash表key
     * @param hashValue hash value
     * @return 写入成功返回true
     */
    public static boolean setHashValue(String hashName, String hashKey, Object hashValue) {
        RedisUtil.hashOperations.put(hashName, hashKey, hashValue);
        return true;
    }

    /**
     * 增加指定的HashKey
     */
    public static Long incrementHashValue(String hashName, String hashKey, Long increment) {
        return RedisUtil.hashOperations.increment(hashName, hashKey, increment);
    }

    /**
     * 删除指定数据
     */
    @SuppressWarnings("unchecked")
    public static void deleteKey(String key) {
        RedisUtil.redisTemplate.delete(key);
    }

}
