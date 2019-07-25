package com.monkeybean.dynamicds.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Getter
@Setter
@ToString
public class TestRecord implements Serializable {
    private static final long serialVersionUID = -3953740355459708922L;

    /**
     * 自增索引
     */
    private Long id;

    /**
     * 记录Id
     */
    private String recordId;

    /**
     * 记录数据
     */
    private String recordData;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;
}
