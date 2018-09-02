package cn.it.ssm.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLimit {

    /**
     * 限制策略，格式示例：5,60  每5秒可以访问60次
     *
     * @return
     */
    String value() default "";

}
