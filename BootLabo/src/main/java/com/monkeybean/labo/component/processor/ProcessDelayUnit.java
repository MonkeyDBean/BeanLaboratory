package com.monkeybean.labo.component.processor;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列处理单元, 仅测试
 * <p>
 * Created by MonkeyBean on 2019/7/11.
 */
public class ProcessDelayUnit implements Delayed {

    /**
     * 重试次数
     */
    private int retryNumber;

    /**
     * 延迟时间, 毫秒
     */
    private long delayTime;

    /**
     * 到期时间, 毫秒
     */
    private long expireTime;

    /**
     * 对象初始化的时间戳
     */
    private long initTimeStamp;

    /**
     * 自定义的处理数据, 如测试请求资源的url
     */
    private String custom;

    /**
     * 对象标识
     */
    private String uid;

    public ProcessDelayUnit() {
        this.retryNumber = 0;
        this.initTimeStamp = 0;
        this.delayTime = 0;
        this.expireTime = System.currentTimeMillis();
    }

    public ProcessDelayUnit(long delayTime, long initTimeStamp, String custom, String uid) {
        this.retryNumber = 0;
        this.delayTime = delayTime;
        this.expireTime = System.currentTimeMillis() + delayTime;
        this.initTimeStamp = initTimeStamp;
        this.custom = custom;
        this.uid = uid;
    }

    public int getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(int retryNumber) {
        this.retryNumber = retryNumber;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 剩余时间=到期时间-当前时间
     */
    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.expireTime, TimeUnit.MILLISECONDS) - timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 队列里的优先级规则
     */
    @Override
    public int compareTo(Delayed other) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
    }

}
