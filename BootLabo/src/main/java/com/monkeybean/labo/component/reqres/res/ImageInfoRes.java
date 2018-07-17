package com.monkeybean.labo.component.reqres.res;

/**
 * Created by MonkeyBean on 2018/7/5.
 */
public class ImageInfoRes {

    /**
     * 图片id
     */
    private int id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件描述
     */
    private String fileDes;

    /**
     * 访问路径
     */
    private String accessUrl;

    /**
     * 是否共享
     */
    private boolean isShare;

    /**
     * 上传时间
     */
    private String uploadTime;

    public ImageInfoRes() {
    }

    public ImageInfoRes(int id, String fileName, String fileDes, String accessUrl, boolean isShare, String uploadTime) {
        this.id = id;
        this.fileName = fileName;
        this.fileDes = fileDes;
        this.accessUrl = accessUrl;
        this.isShare = isShare;
        this.uploadTime = uploadTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDes() {
        return fileDes;
    }

    public void setFileDes(String fileDes) {
        this.fileDes = fileDes;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
