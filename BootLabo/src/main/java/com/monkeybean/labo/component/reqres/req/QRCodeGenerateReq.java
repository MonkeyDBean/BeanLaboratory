package com.monkeybean.labo.component.reqres.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/8/2.
 */
public class QRCodeGenerateReq {

    @NotNull
    @Size(max = 200000, message = "内容长度非法")
    private String content;

    @Pattern(regexp = "^png|jpg|jpeg$", message = "图片格式非法")
    @NotNull
    private String format;

    @Max(10000)
    private int width;

    @Max(10000)
    private int height;

    @NotNull
    private boolean hasBlank;

    @NotNull
    private boolean hasLogo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isHasBlank() {
        return hasBlank;
    }

    public void setHasBlank(boolean hasBlank) {
        this.hasBlank = hasBlank;
    }

    public boolean isHasLogo() {
        return hasLogo;
    }

    public void setHasLogo(boolean hasLogo) {
        this.hasLogo = hasLogo;
    }

}
