package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.*;

/**
 * Created by MonkeyBean on 2018/7/5.
 */
public class ImageInfoChangeReq {

    @Pattern(regexp = ConstValue.LEGAL_POSITIVE_INT, message = "图片id")
    @Max(Integer.MAX_VALUE)
    @Min(1)
    @NotNull
    private String id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String des;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdInt() {
        return Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
