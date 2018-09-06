package com.monkeybean.socket.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "chat_record")
public class ChatRecord {
    /**
     * 自增索引
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 消息类型, 聊天为0，加入房间为1，离开房间为2
     */
    @Column(name = "message_type")
    private Integer messageType;

    /**
     * 消息内容
     */
    @Column(name = "message_content")
    private String messageContent;

    /**
     * 记录生成时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public ChatRecord() {
    }

    /**
     * 构造器
     *
     * @param userName       用户名
     * @param messageType    消息类型
     * @param messageContent 消息内容
     */
    public ChatRecord(String userName, Integer messageType, String messageContent) {
        if (userName == null) {
            userName = "";
        }
        if (messageContent == null) {
            messageContent = "";
        }
        this.userName = userName.length() <= 255 ? userName : userName.substring(0, 255);
        this.messageType = messageType;
        this.messageContent = messageContent.length() <= 4096 ? messageContent : messageContent.substring(0, 4096);
        this.createTime = new Date();
    }

    /**
     * 获取自增索引
     *
     * @return id - 自增索引
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增索引
     *
     * @param id 自增索引
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取消息类型, 聊天为0，加入房间为1，离开房间为2
     *
     * @return message_type - 消息类型, 聊天为0，加入房间为1，离开房间为2
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置消息类型, 聊天为0，加入房间为1，离开房间为2
     *
     * @param messageType 消息类型, 聊天为0，加入房间为1，离开房间为2
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取消息内容
     *
     * @return message_content - 消息内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置消息内容
     *
     * @param messageContent 消息内容
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 获取记录生成时间
     *
     * @return create_time - 记录生成时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置记录生成时间
     *
     * @param createTime 记录生成时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取记录更新时间
     *
     * @return update_time - 记录更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置记录更新时间
     *
     * @param updateTime 记录更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}