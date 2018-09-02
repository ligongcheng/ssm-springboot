package cn.it.ssm.config;

import cn.it.ssm.common.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class SpringCacheKeyGenerator implements KeyGenerator {
    private final static String NO_PARAM_KEY = "@";
    //TODO key前缀，用于区分不同项目的缓存，建议每个项目单独设置
    private String keyPrefix;

    @Override
    public Object generate(Object target, Method method, Object... params) {
        char sp = ':';
        StringBuilder strBuilder = new StringBuilder(30);
        if (StringUtils.isNotBlank(keyPrefix)) {
            strBuilder.append(keyPrefix);
            strBuilder.append(sp);
        }
        // 类名
        String name = target.getClass().getName();
        String[] split = StringUtils.split(name, ".");
        int length = split.length;
        for (int i = 0; i < length - 1; i++) {
            strBuilder.append(split[i]);
            strBuilder.append(".");
        }
        String className = split[length - 1];
        if (className.contains("$")) {
            className = className.substring(0, className.indexOf("$"));
        }
        strBuilder.append(className);
        strBuilder.append(sp);
        // 方法名
        strBuilder.append(method.getName());
        strBuilder.append(sp);
        if (params.length > 0) {
            // 参数值
           /* for (Object object : params) {
                if (BeanHelper.isSimpleValueType(object.getClass())) {
                    strBuilder.append(object);
                } else {
                    strBuilder.append(JsonHelper.toJSONString(object).hashCode());
                }
            }*/
            for (Object object : params) {
                strBuilder.append(JsonHelper.toJSONString(object).hashCode());
            }
        } else {
            strBuilder.append(NO_PARAM_KEY);
        }
        return strBuilder.toString();
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
