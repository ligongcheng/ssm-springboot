package cn.it.ssm.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by James on 2017-05-24.
 * From 咕泡学院出品
 * 老师咨询 QQ 2904270631
 */
public class RedisVo implements Serializable {
    private Date date;
    private Integer count;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
