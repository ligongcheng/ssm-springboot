package cn.it.ssm.common.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回响应数据
 *
 * @author Jacky
 */
public class ConResult implements Serializable {

    private int code;

    private String msg;

    private Map<String, Object> data = new HashMap<>();

    public ConResult() {
        super();
    }

    public ConResult(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ConResult(int code, String msg, Map<String, Object> data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ConResult success() {
        return new ConResult(200, "success");
    }

    public static ConResult error() {
        return new ConResult(400, "error");
    }

    public ConResult addData(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public ConResult addMsg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


}
