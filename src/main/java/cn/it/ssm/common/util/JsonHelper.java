package cn.it.ssm.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonHelper {

    /**
     * Java对象序列化为JSON字符串
     *
     * @param obj Java对象
     * @return json字符串
     */
    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj,
            SerializerFeature.WriteClassName,
            SerializerFeature.DisableCircularReferenceDetect
        );
    }

    /**
     * JSON字符串反序列化为Java对象
     *
     * @param jsonStr JSON字符串
     * @param clazz   java对象的class
     * @return java对象
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return JSON.parseObject(jsonStr, clazz);
    }
}
