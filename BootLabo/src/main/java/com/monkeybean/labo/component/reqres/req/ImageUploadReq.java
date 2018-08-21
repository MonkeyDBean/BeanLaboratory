package com.monkeybean.labo.component.reqres.req;

import com.monkeybean.labo.predefine.ConstValue;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
public class ImageUploadReq {

    /**
     * base64编码的图片
     * 限制强度：NotBlank > NotEmpty > NotNull
     */
    @NotBlank
    private String file64;

    /**
     * 文件名
     */
    @Pattern(regexp = ConstValue.LEGAL_IMAGE_NAME, message = "必须为合法图片名")
    @NotNull
    @Size(max = 50)
    private String name;

    public String getFile64() {
        return file64;
    }

    public void setFile64(String file64) {
        this.file64 = file64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
