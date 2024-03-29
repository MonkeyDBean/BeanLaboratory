package com.monkeybean.labo.component.reqres.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by MonkeyBean on 2018/7/6.
 */
public class ImageStatusChangeReq {

    @NotNull
    private List<Integer> id;

    @Pattern(regexp = "^[12]$", message = "操作类型不合法")
    @NotNull
    private String operate;

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public int getOperateInt() {
        return Integer.parseInt(operate);
    }
}
