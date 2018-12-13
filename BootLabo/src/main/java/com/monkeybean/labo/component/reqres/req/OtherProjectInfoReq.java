package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by MonkeyBean on 2018/7/7.
 */
public class OtherProjectInfoReq {

    @Pattern(regexp = "^[0123]$", message = "类型不合法")
    @NotNull
    private String type;

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

    @Pattern(regexp = ConstValue.LEGAL_UNSIGNED_INT, message = "总记录数，格式不合法")
    @Max(Integer.MAX_VALUE)
    @Min(0)
    @NotNull
    private String total;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getTypeInt() {
        return Integer.parseInt(type);
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
