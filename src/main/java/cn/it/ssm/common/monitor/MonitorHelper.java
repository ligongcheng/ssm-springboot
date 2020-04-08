package cn.it.ssm.common.monitor;

import cn.it.ssm.common.entity.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * Created by on 17/3/14. Description:
 */
@Component
public class MonitorHelper {

    private static final Logger logger = LoggerFactory.getLogger(MonitorHelper.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public void count(final String uri) {

        Set<String> cacheMappingUri = getCacheMappingUri();
        if (cacheMappingUri == null || !cacheMappingUri.contains(uri)) {
            return;
        }
        final long seconds = System.currentTimeMillis() / 1000;
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (Integer prec : RedisConstants.PRECISION) {
                    long startSlice = seconds / prec * prec;
                    String hash = String.format("%s:%s", prec, uri);
                    //connection.zAdd("known:".getBytes(), 0, hash.getBytes());
                    connection.hIncrBy((ApiEnum.API_COUNT_PREFIX.getApiValue() + hash).getBytes(), String.valueOf(startSlice).getBytes(), 1L);
                }
                logger.debug("监控记数执行完成for {}", uri);
                return null;
            }
        });

    }

    public Set<String> getCacheMappingUri() {
        Set<String> set = stringRedisTemplate.opsForZSet().range(ApiEnum.API_URI.getApiValue(), 0, -1);
        return set;

    }

}
