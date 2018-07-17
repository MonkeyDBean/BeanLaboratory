package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class PwdResetReq {

    @Pattern(regexp = ConstValue.LEGAL_PHONE, message = "必须为合法手机号")
    @NotNull
    private String phone;

    /**
     * 短信验证码
     */
    @NotEmpty(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度不合法")
    private String code;

    /**
     * 单次md5密码，固定32位
     */
    @Size(min = 32, max = 32, message = "新密码不合法")
    @NotNull
    private String pwd;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
