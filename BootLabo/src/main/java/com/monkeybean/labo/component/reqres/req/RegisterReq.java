package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class RegisterReq {

    /**
     * 大陆手机号
     */
    @Pattern(regexp = ConstValue.LEGAL_PHONE, message = "必须为合法手机号")
    @NotNull
    private String phone;

    @NotEmpty(message = "短信验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度不合法")
    private String code;

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名必须在2-20个字符之间")
    private String name;

    /**
     * 单次md5
     */
    @Size(min = 32, max = 32, message = "密码不合法")
    @NotNull
    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
