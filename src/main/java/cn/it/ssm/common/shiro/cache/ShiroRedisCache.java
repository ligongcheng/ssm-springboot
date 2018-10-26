package cn.it.ssm.common.shiro.cache;

import cn.it.ssm.domain.auto.SysUser;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class ShiroRedisCache<K, V> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(ShiroRedisCache.class);

    private HashOperations<String, String, Object> hashOperations;
    private String prefix = "shiro_redis:";

    public ShiroRedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public ShiroRedisCache(RedisTemplate redisTemplate, String prefix) {
        this(redisTemplate);
        this.prefix = prefix;
    }


    @Override
    public V get(K key) throws CacheException {
        if (log.isDebugEnabled()) {
            log.warn("getKey: {}", key);
        }
        log.warn("getKey: {}", key);
        if (key == null) {
            return null;
        }
        Object cacheKey = getRedisCacheKey(key);
        V vl = (V) hashOperations.get(prefix, cacheKey);
        return vl;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (log.isDebugEnabled()) {
            log.warn("putKey: {}, value: {}", key, value);
        }
        log.warn("putKey: {}, value: {}", key, value);
        if (key == null || value == null) {
            return null;
        }
        String cacheKey = getRedisCacheKey(key);
        hashOperations.put(prefix, cacheKey, value);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Key: {}", key);
        }

        if (key == null) {
            return null;
        }
        String cacheKey = getRedisCacheKey(key);
        V v = (V) hashOperations.get(prefix, cacheKey);
        hashOperations.delete(prefix, cacheKey);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        hashOperations.delete(prefix, keys());
    }

    @Override
    public int size() {
        Long size = hashOperations.size(prefix);
        return size.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<K> keys = null;
        try {
            keys = (Set<K>) hashOperations.keys(prefix);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("keys: {} ,error", prefix);
            }
        }
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = null;
        try {
            values = (List<V>) hashOperations.values(prefix);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("values: {} ,error", prefix);
            }
        }
        return values;
    }

    private String getRedisCacheKey(K key) {
        String redisKey = null;
        if (key == null) {
            return null;
        }
        if (key instanceof PrincipalCollection) {
            Object primaryPrincipal = ((PrincipalCollection) key).getPrimaryPrincipal();
            SysUser sysUser = (SysUser) primaryPrincipal;
            redisKey = sysUser.getUsername();
        } else {
            redisKey = key.toString();
        }
        return redisKey;

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}