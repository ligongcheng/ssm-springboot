package cn.it.ssm.common.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

public class ShiroRedisCacheManager extends AbstractCacheManager {

    private RedisTemplate shiroRedisTemplate;

    public ShiroRedisCacheManager(RedisTemplate shiroRedisTemplate) {
        this.shiroRedisTemplate = shiroRedisTemplate;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        ShiroRedisCache cache = new ShiroRedisCache(shiroRedisTemplate, name);
        return cache;
    }
}
