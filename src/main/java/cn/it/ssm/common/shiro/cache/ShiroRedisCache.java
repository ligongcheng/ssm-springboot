package cn.it.ssm.common.shiro.cache;

import cn.it.ssm.domain.auto.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class ShiroRedisCache<K, V> implements Cache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(ShiroRedisCache.class);

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> ops;
    private String prefix;
    private long timeout;

    public ShiroRedisCache(RedisTemplate redisTemplate, String prefix, long timeout) {
        this.redisTemplate = redisTemplate;
        this.ops = redisTemplate.opsForValue();
        this.prefix = prefix;
        this.timeout = timeout;
    }


    @Override
    public V get(K key) throws CacheException {
        log.debug("getKey: {}", key);
        if (key == null) {
            return null;
        }
        Object cacheKey = getRedisCacheKey(key);
        V vl = (V) ops.get(cacheKey);
        return vl;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        log.debug("putKey: {}, value: {}", key, value);
        if (key == null || value == null) {
            return null;
        }
        String cacheKey = getRedisCacheKey(key);
        ops.set(cacheKey, value, timeout, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        log.debug("removeKey: {}", key);

        if (key == null) {
            return null;
        }
        String cacheKey = getRedisCacheKey(key);
        V v = get(key);
        ops.getOperations().delete(cacheKey);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        log.debug("clear cache");
        try {
            redisTemplate.delete((Collection<String>) keys());
        } catch (Exception e) {

        }
    }

    @Override
    public int size() {
        log.debug("size");
        return keys().size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        log.debug("keys");
        Set<Object> objkeys = null;
        Set<K> ks = Collections.emptySet();
        try {
            objkeys = redisTemplate.execute(new RedisCallback<Set<Object>>() {

                @Override
                public Set<Object> doInRedis(RedisConnection connection) throws DataAccessException {

                    Set<Object> binaryKeys = new HashSet<>();

                    Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(prefix + "*").count(10000).build());
                    while (cursor.hasNext()) {
                        binaryKeys.add(new String(cursor.next()));
                    }
                    return binaryKeys;
                }
            });
        } catch (Exception e) {
            log.debug("keys: {} ,error", prefix);
        }
        if (CollectionUtils.isEmpty(objkeys) || !prefix.contains("session")) {
            return Collections.emptySet();
        }
        try {
            ks = (Set<K>) objkeys;
        } catch (Exception e) {
        }
        return ks;
    }

    @Override
    public Collection<V> values() {
        log.debug("session values");
        List<V> values = null;
        try {
            values = (List<V>) ops.multiGet((Collection<String>) keys());
        } catch (Exception e) {
            log.debug("values: {} ,error", prefix);
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
            redisKey = prefix + sysUser.getUsername();
        } else {
            redisKey = prefix + key.toString();
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