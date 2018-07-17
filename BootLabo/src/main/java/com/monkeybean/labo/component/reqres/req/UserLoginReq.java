package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class UserLoginReq {

    @Pattern(regexp = ConstValue.LEGAL_PHONE + "|" + ConstValue.LEGAL_MAIL, message = "合法大陆手机号或邮箱")
    @NotNull
    private String user;

    /**
     * n+1次md5，固定32位
     */
    @Size(min = 32, max = 32, message = "密码不合法")
    @NotNull
    private String pwd;

    /**
     * google reCaptcha response
     */
    @NotNull
    private String response;

    @Pattern(regexp = ConstValue.LEGAL_BOOLEAN, message = "必须为boolean类型，不区分大小写")
    @NotNull
    private String stay;

    /**
     * 生成签名的时间
     */
    @Pattern(regexp = ConstValue.LEGAL_TIMESTAMP, message = "必须为合法时间戳，毫秒")
    @NotNull
    private String stime;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStay() {
        return stay;
    }

    public void setStay(String stay) {
        this.stay = stay;
    }

    public boolean getStayBoolean() {
        return Boolean.parseBoolean(stay);
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }
}