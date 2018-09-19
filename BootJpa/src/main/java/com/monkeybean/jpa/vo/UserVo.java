package com.monkeybean.jpa.vo;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
public class UserVo {
    private Long userId;
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
