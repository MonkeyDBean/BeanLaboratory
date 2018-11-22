package com.monkeybean.mvc.entity;

import java.io.Serializable;

/**
 * Created by MonkeyBean on 2018/11/19.
 */
public class TestRecord implements Serializable {

    /**
     * 记录Id
     */
    private String recordId;

    /**
     * 记录数据
     */
    private String recordData;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordData() {
        return recordData;
    }

    public void setRecordData(String recordData) {
        this.recordData = recordData;
    }
}
