package cn.it.ssm.common.entity;

import java.io.Serializable;

/**
 * 返回响应数据
 *
 * @author Jacky
 */
public class ConResult<T> implements Serializable {

    private static final long serialVersionUID = -5533008635619163790L;

    private int code;

    private String msg;

    private T data;

    public ConResult() {
    }

    public ConResult(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ConResult(int code, String msg, T data) {
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

    public ConResult<T> addData(T value) {
        this.setData(value);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
