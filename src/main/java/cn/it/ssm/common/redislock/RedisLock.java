package cn.it.ssm.common.redislock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RedisLock {
    private static StringRedisTemplate stringRedisTemplate;

    private static ResourceScriptSource unlockScriptSource;

    private static ResourceScriptSource checkScriptSource;

    /**
     * 尝试获取锁间隔时间，避免发送大量redis请求 默认 20-40 毫秒
     */
    public long acquireIdle = 20L;
    /**
     * 重新设置锁过期时间间隔
     */
    public long checkIdle = 1000L;

    /**
     * 是否启动新线程重设锁过期时间
     */
    public Boolean isCheck = true;

    private volatile AtomicBoolean checkFlag = new AtomicBoolean(false);

    private RedisLock() {
    }

    public static RedisLock instance() {
        if (stringRedisTemplate == null) throw new RuntimeException("请设置stringRedisTemplate");
        return new RedisLock();
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisLock.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * @param key
     * @param value
     * @param expire  key 过期时间 毫秒
     * @param timeOut 获取锁超时时间 毫秒
     * @return 获取锁结果 成功 true 失败 false
     */
    public Boolean lock(String key, String value, long expire, long timeOut) {
        Boolean lock = false;
        if (StringUtils.isEmpty(key)) throw new RuntimeException("key must not empty");
        if (StringUtils.isEmpty(value)) throw new RuntimeException("value must not empty");
        if (timeOut > 0) {
            long start = System.currentTimeMillis();
            try {
                while (!lock && System.currentTimeMillis() - start <= timeOut) {
                    lock = doLock(key, value, expire, lock);
                    if (lock) break;
                    //不同线程停顿时间不同,避免同时开始获得锁
                    Thread.sleep((long) (acquireIdle * (Math.random() + 1)));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                while (!lock) {
                    lock = doLock(key, value, expire, lock);
                    if (lock) break;
                    Thread.sleep((long) (acquireIdle * (Math.random() + 1)));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (lock && expire > 0) {
            if (isCheck) {
                checkFlag.set(true);
                checkThread(key, value, expire, stringRedisTemplate);
            }
        }
        return lock;
    }

    private Boolean doLock(String key, String value, long expire, Boolean lock) {
        if (expire > 0) {
            lock = stringRedisTemplate.opsForValue().setIfAbsent(key, value, expire, TimeUnit.MILLISECONDS);
        } else {
            lock = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        }
        return lock;
    }

    public Boolean tryLockWithExp(String key, String value, Long expire, Long timeOut) {
        return lock(key, value, expire, timeOut);
    }

    public Boolean tryLockWithOutExp(String key, String value, Long timeOut) {
        return lock(key, value, -1, timeOut);
    }

    public Boolean lockWithOutExp(String key, String value) {
        return lock(key, value, -1, -1);
    }

    public Boolean lockWithExp(String key, String value, Long expire) {
        return lock(key, value, expire, -1);
    }

    public Boolean unlock(String key, String value) {
        try {
            if (unlockScriptSource == null) {
                unlockScriptSource = new ResourceScriptSource(new ClassPathResource("script/unlock.lua"));
            }
            DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript(unlockScriptSource.getScriptAsString(), Boolean.class);
            List<String> keyList = Arrays.asList(key);
            Boolean res = stringRedisTemplate.execute(defaultRedisScript, keyList, value);
            if (isCheck) checkFlag.set(false);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void checkThread(String key, String value, Long expire, StringRedisTemplate stringRedisTemplate) {
        new Thread(() -> {
            try {
                while (checkFlag.get()) {
                    Thread.sleep(checkIdle);
                    if (checkScriptSource == null) {
                        checkScriptSource = new ResourceScriptSource(new ClassPathResource("script/unlock.lua"));
                    }
                    DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript(checkScriptSource.getScriptAsString(), Boolean.class);
                    List<String> keyList = Arrays.asList(key, expire.toString());
                    Boolean res = stringRedisTemplate.execute(defaultRedisScript, keyList, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public long getAcquireIdle() {
        return acquireIdle;
    }

    public void setAcquireIdle(long acquireIdle) {
        this.acquireIdle = acquireIdle;
    }

    public long getCheckIdle() {
        return checkIdle;
    }

    public void setCheckIdle(long checkIdle) {
        this.checkIdle = checkIdle;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }
}
