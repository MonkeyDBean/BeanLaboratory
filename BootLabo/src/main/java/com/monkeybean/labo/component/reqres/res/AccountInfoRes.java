package com.monkeybean.labo.component.reqres.res;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class AccountInfoRes {

    /**
     * 账户手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否为管理员
     */
    private boolean admin;

    public AccountInfoRes() {
    }

    public AccountInfoRes(String phone, String nickname, String email, String avatar, boolean admin) {
        this.phone = phone;
        this.nickname = nickname;
        this.email = email;
        this.avatar = avatar;
        this.admin = admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
