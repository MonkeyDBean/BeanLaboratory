package com.monkeybean.jpa.res;

/**
 * 响应结果生成工具
 * <p>
 * Created by MonkeyBean on 2018/9/7.
 */
public class ResultGenerator {

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS.code)
                .setMessage(ResultCode.SUCCESS.message);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS.code)
                .setMessage(ResultCode.SUCCESS.message)
                .setData(data);
    }

    public static Result genFailResult(ResultCode code) {
        return new Result()
                .setCode(code.code)
                .setMessage(code.message);
    }
}
