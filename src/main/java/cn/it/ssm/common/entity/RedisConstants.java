package cn.it.ssm.common.entity;

import java.io.Serializable;

/**
 * Created by
 * on 17/3/15.
 * Description:
 */
public class RedisConstants implements Serializable {

    //单位时间
    public static final Integer[] PRECISION = new Integer[]{5, 30, 60, 300, 3600, 18000, 86400};
}
