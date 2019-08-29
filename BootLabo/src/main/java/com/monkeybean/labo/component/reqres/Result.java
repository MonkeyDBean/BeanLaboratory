package com.monkeybean.labo.component.reqres;

import com.monkeybean.labo.predefine.ReturnCode;

/**
 * 数据返回格式
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
public class Result<T> {
    private int code;
    private String msg;
    private String des;
    private T data;

    public Result() {
    }

    public Result(ReturnCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.des = statusCode.getDescription();
    }

    public Result(ReturnCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.des = statusCode.getDescription();
        this.data = data;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
