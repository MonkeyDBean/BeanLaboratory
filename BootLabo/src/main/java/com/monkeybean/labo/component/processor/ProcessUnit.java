package com.monkeybean.labo.component.processor;

/**
 * 阻塞队列处理单元, 仅测试
 * <p>
 * Created by MonkeyBean on 2019/7/11.
 */
public class ProcessUnit {

    /**
     * 对象标识
     */
    private String uid;

    /**
     * 对象初始化的时间戳
     */
    private long initTimeStamp;

    /**
     * 自定义的处理数据, 如测试请求资源的url
     */
    private String custom;

    public ProcessUnit() {
    }

    public ProcessUnit(String uid, long initTimeStamp, String custom) {
        this.uid = uid;
        this.initTimeStamp = initTimeStamp;
        this.custom = custom;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getInitTimeStamp() {
        return initTimeStamp;
    }

    public void setInitTimeStamp(long initTimeStamp) {
        this.initTimeStamp = initTimeStamp;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
