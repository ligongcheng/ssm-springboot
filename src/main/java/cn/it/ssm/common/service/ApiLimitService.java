package cn.it.ssm.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiLimitService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private DefaultRedisScript<Long> script = initScript();
    private RedisSerializer Ser = new StringRedisSerializer();

    private DefaultRedisScript<Long> initScript(){
        DefaultRedisScript<Long> script = new DefaultRedisScript<Long>();
        script.setResultType(Long.class);
        script.setLocation(new ClassPathResource("script/apiLimit.lua"));
        return script;
    }
    public boolean isLimited(List<String> keys, String... args) {
        Long aLong = redisTemplate.execute(script, keys, args[0],args[1]);
        return aLong != 1;
    }

}
