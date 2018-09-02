package cn.it.ssm.common.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
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

    private HashOperations<String, K, V> hashOperations;
    private String prefix = "shiro_redis:";

    public ShiroRedisCache(RedisTemplate<K, V> redisTemplate) {
        this.hashOperations = (HashOperations<String, K, V>) redisTemplate.opsForHash();
    }

    public ShiroRedisCache(RedisTemplate<K, V> redisTemplate, String prefix) {
        this(redisTemplate);
        this.prefix = prefix;
    }


    @Override
    public V get(K key) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Key: {}", key);
        }
        if (key == null) {
            return null;
        }

        V vl = hashOperations.get(prefix, key);

        return vl;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Key: {}, value: {}", key, value);
        }

        if (key == null || value == null) {
            return null;
        }
        hashOperations.put(prefix, key, value);
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

        V v = hashOperations.get(prefix, key);
        hashOperations.delete(prefix, key);
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
        Set<K> keys = hashOperations.keys(prefix);
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = hashOperations.values(prefix);
        return values;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}