package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class MessageApplyReq {

    /**
     * 大陆手机号
     */
    @Pattern(regexp = ConstValue.LEGAL_PHONE, message = "必须为合法手机号")
    @NotNull
    private String phone;

    @NotEmpty(message = "图形验证码不能为空")
    @Size(min = 4, max = 4, message = "验证码长度不合法")
    private String code;

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
