package cn.it.ssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class SpringRedisCacheConfig {

    private RedisConnectionFactory redisConnectionFactory;

    public SpringRedisCacheConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()).build();
        return cacheManager;
    }

    @Bean
    public SpringCacheKeyGenerator kg() {
        SpringCacheKeyGenerator keyGenerator = new SpringCacheKeyGenerator();
        //TODO 请根据不同项目修改不同值来区分
        //keyGenerator.setKeyPrefix("请根据不同项目修改不同值来区分");
        return keyGenerator;
    }
}
