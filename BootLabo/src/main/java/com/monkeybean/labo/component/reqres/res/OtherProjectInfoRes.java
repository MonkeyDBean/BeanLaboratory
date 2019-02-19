package com.monkeybean.labo.component.reqres.res;

/**
 * Created by MonkeyBean on 2018/7/7.
 */
public class OtherProjectInfoRes {

    /**
     * 记录标识
     */
    private int id;

    /**
     * 名称
     */
    private String name;

    /**
     * 链接
     */
    private String url;

    /**
     * 预览图
     */
    private String image;

    /**
     * 简介
     */
    private String des;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
