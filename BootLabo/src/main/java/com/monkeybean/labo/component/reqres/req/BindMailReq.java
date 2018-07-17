package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by MonkeyBean on 2018/06/02.
 */
public class BindMailReq {

    @Pattern(regexp = ConstValue.LEGAL_MAIL, message = "必须为合法邮箱地址")
    @NotNull
    private String mail;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
