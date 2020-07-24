package cn.it.ssm.common.redislock;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
    /**
     * 存入redis的key
     *
     * @return
     */
    String key() default "";

    /**
     * 存入redis的值
     *
     * @return
     */
    String value() default "";

    /**
     * 锁过期时间 默认为-1即不过期 单位 毫秒
     *
     * @return
     */
    long expire() default -1;

    /**
     * 加锁超时时间 默认为-1即一直获取锁，会阻塞线程！ 单位 毫秒
     *
     * @return
     */
    long timeOut() default -1;

    /**
     * 尝试获取锁间隔时间，避免发送大量redis请求 默认 20-40 毫秒之间
     */
    long acquireIdle() default 20L;

    /**
     * 重新设置锁过期时间间隔
     */
    long checkIdle() default 1000L;

    /**
     * 是否启动新线程重设锁过期时间
     */
    boolean isCheck() default true;
}
