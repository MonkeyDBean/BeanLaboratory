package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2019/2/18.
 */
public class OtherProjectInfoAddReq {

    /**
     * 项目类型
     */
    @Pattern(regexp = "^[0123]$", message = "类型不合法")
    @NotNull
    private String type;

    /**
     * 项目名称
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * 访问链接
     */
    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = ConstValue.LEGAL_URL)
    private String url;

    /**
     * 缩略图url
     */
    @Size(min = 12, max = 255)
    @Pattern(regexp = ConstValue.LEGAL_URL)
    private String image;

    /**
     * 项目描述
     */
    @Size(min = 1, max = 255)
    private String des;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getTypeInt() {
        return Integer.parseInt(type);
    }
}
