package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by MonkeyBean on 2018/7/3.
 */
public class ImageListReq {

    @Pattern(regexp = "^[012]$", message = "类型不合法")
    @NotNull
    private String sharetype;

    @Pattern(regexp = ConstValue.LEGAL_POSITIVE_INT, message = "当前页，格式不合法")
    @Max(Integer.MAX_VALUE)
    @Min(1)
    @NotNull
    private String current;

    @Pattern(regexp = ConstValue.LEGAL_POSITIVE_INT, message = "每页记录数，格式不合法")
    @Max(Integer.MAX_VALUE)
    @Min(1)
    @NotNull
    private String size;

    public String getSharetype() {
        return sharetype;
    }

    public void setSharetype(String sharetype) {
        this.sharetype = sharetype;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSharetypeInt() {
        return Integer.parseInt(sharetype);
    }

    public int getCurrentInt() {
        return Integer.parseInt(current);
    }

    public int getSizeInt() {
        return Integer.parseInt(size);
    }
}
