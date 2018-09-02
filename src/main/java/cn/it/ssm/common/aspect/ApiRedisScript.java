package cn.it.ssm.common.aspect;

import org.springframework.data.redis.core.script.RedisScript;

public class ApiRedisScript implements RedisScript<Long> {
    @Override
    public String getSha1() {
        return null;
    }

    @Override
    public Class<Long> getResultType() {
        return null;
    }

    @Override
    public String getScriptAsString() {
        return null;
    }
}
