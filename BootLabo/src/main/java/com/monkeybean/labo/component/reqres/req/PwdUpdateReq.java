package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class PwdUpdateReq {

    /**
     * n+1次md5密码，固定32位
     */
    @Size(min = 32, max = 32, message = "老密码不合法")
    @NotNull
    private String oldpwd;

    /**
     * 单次md5密码，固定32位
     */
    @Size(min = 32, max = 32, message = "新密码不合法")
    @NotNull
    private String newpwd;

    /**
     * 生成签名的时间
     */
    @NotEmpty(message = "生成签名的时间不能为空")
    @Pattern(regexp = ConstValue.LEGAL_TIMESTAMP, message = "必须为合法时间戳，毫秒")
    private String stime;

    public String getOldpwd() {
        return oldpwd;
    }

    public void setOldpwd(String oldpwd) {
        this.oldpwd = oldpwd;
    }

    public String getNewpwd() {
        return newpwd;
    }

    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }
}
