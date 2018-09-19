package com.monkeybean.jpa.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
//用来标识父类, @MappedSuperclass标识的类表示其不能映射到数据库表，因为其不是一个完整的实体类，但是它所拥有的属性能够隐射在其子类对用的数据库表中
//有了@MappedSuperclass，不能再有@Entity或@Table注解
@MappedSuperclass
//用于监听实体类添加或者删除操作
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -7641001287969568244L;

    /**
     * 主键, 常见strategy：
     * AUTO: 主键由程序控制(主键生成策略交给持久化引擎, 持久化引擎会根据数据库在以下三种主键生成策略中选择其中一种)，也是GenerationType的默认值
     * IDENTITY: 主键由数据库自动生成(支持自动增长的数据库，如mysql)
     * TABLE: 使用特定的数据库表格来保存主键, 与@TableGenerator一起使用, 在不同数据库间容易移植，但不能充分利用数据库的特性
     * SEQUENCE: 根据底层数据库的序列来生成主键，条件是数据库支持序列，一般用于不支持主键自增长的数据库如Oracle
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @CreatedDate
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @LastModifiedDate
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
