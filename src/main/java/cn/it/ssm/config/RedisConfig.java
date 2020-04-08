package cn.it.ssm.config;

import cn.it.ssm.common.util.FastJsonRedisSerializer;
import com.alibaba.fastjson.parser.ParserConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class RedisConfig {

    /**
     * 覆盖自动配置的redisTemplate，修改serializer
     *
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
        RedisConnectionFactory redisConnectionFactory,
        StringRedisTemplate stringRedisTemplate) throws UnknownHostException {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        ParserConfig.getGlobalInstance().addAccept("cn.it.ssm");
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        RedisHolder.setRedisTemplate(template);
        RedisHolder.setStringRedisTemplate(stringRedisTemplate);
        return template;
    }

    /**
     * shiroRedisTemplate ValueSerializer无法使用jackson2JsonRedisSerializer 会无法反序列化
     *
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public RedisTemplate<Object, Object> shiroRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        RedisHolder.setShiroRedisTemplate(template);
        return template;
    }

    @Bean
    public RedisTemplate<Object, Object> redisJsonTemplate(
        RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {

        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);// 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        RedisHolder.setRedisJsonTemplate(template);
        return template;
    }
}
