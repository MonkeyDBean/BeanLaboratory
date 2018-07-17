package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by zhangbin on 2018/7/3.
 */
public class ImageListReq {

    @Pattern(regexp = "^[012]$", message = "类型不合法")
    @NotNull
    private String sharetype;

    @Pattern(regexp = ConstValue.LEGAL_POSITIVE_INT, message = "当前页，格式不合法")
    @Max(Integer.MAX_VALUE)
    @NotNull
    private String current;

    @Pattern(regexp = ConstValue.LEGAL_POSITIVE_INT, message = "每页记录数，格式不合法")
    @Max(Integer.MAX_VALUE)
    @NotNull
    private String size;

    @Pattern(regexp = ConstValue.LEGAL_UNSIGNED_INT, message = "总记录数，格式不合法")
    @Max(Integer.MAX_VALUE)
    @NotNull
    private String total;

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public int getTotalInt() {
        return Integer.parseInt(total);
    }
}
