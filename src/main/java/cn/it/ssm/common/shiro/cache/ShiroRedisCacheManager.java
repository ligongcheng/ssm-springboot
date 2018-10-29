package cn.it.ssm.common.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;

public class ShiroRedisCacheManager extends AbstractCacheManager {

    private RedisTemplate shiroRedisTemplate;
    private HashMap<String, Long> timeMap;
    private long globalTimeout = 1800;

    public ShiroRedisCacheManager(RedisTemplate shiroRedisTemplate) {
        this.shiroRedisTemplate = shiroRedisTemplate;
    }

    @Override
    protected Cache createCache(String name) throws CacheException {
        Long aLong = null;
        if (timeMap != null && timeMap.size() > 0) {
            if (timeMap.containsKey(name)) {
                aLong = timeMap.get(name);
            }
        }
        ShiroRedisCache cache = new ShiroRedisCache(shiroRedisTemplate, name, aLong != null ? aLong : globalTimeout);
        return cache;
    }

    public HashMap<String, Long> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(HashMap<String, Long> timeMap) {
        this.timeMap = timeMap;
    }

    public long getGlobalTimeout() {
        return globalTimeout;
    }

    public void setGlobalTimeout(long globalTimeout) {
        this.globalTimeout = globalTimeout;
    }
}
