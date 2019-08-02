package com.monkeybean.dynamicds.reqres;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Getter
@Setter
public class Result<T> {
    private int code;
    private String des;
    private T data;

    public Result(ReturnCode statusCode) {
        this.code = statusCode.getCode();
        this.des = statusCode.getDescription();
    }

    public Result(ReturnCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.des = statusCode.getDescription();
        this.data = data;
    }
}
