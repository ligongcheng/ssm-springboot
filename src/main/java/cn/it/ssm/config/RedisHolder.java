package cn.it.ssm.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisHolder {

    private static RedisTemplate redisTemplate = null;

    private static RedisTemplate shiroRedisTemplate = null;

    private static RedisTemplate redisJsonTemplate = null;

    private static StringRedisTemplate stringRedisTemplate = null;


    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisHolder.redisTemplate = redisTemplate;
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisHolder.stringRedisTemplate = stringRedisTemplate;
    }

    public static RedisTemplate getShiroRedisTemplate() {
        return shiroRedisTemplate;
    }

    public static void setShiroRedisTemplate(RedisTemplate shiroRedisTemplate) {
        RedisHolder.shiroRedisTemplate = shiroRedisTemplate;
    }

    public static RedisTemplate getRedisJsonTemplate() {
        return redisJsonTemplate;
    }

    public static void setRedisJsonTemplate(RedisTemplate redisJsonTemplate) {
        RedisHolder.redisJsonTemplate = redisJsonTemplate;
    }
}
