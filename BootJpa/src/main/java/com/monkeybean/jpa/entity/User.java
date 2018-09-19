package com.monkeybean.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@Entity
//如果缺省@Table注释，系统默认采用实体类名作为映射表的表名
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1816819952991833528L;

    @Column(name = "user_id", unique = true, nullable = false, columnDefinition = "int(11) comment '用户Id'")
    private Long userId;

    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(20) comment '用户昵称'")
    private String userName;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint(1) default 0 comment '是否删除：0否1是'")
    private Boolean isDeleted;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
