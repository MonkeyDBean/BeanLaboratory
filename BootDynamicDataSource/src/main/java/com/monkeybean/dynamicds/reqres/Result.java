package com.monkeybean.dynamicds.reqres;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public class Result<T> {
    private int code;
    private String des;
    private T data;

    public Result() {
    }

    public Result(ReturnCode statusCode) {
        this.code = statusCode.getCode();
        this.des = statusCode.getDescription();
    }

    public Result(ReturnCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.des = statusCode.getDescription();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
