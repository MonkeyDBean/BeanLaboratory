package com.monkeybean.dynamicds.service;

import com.monkeybean.dynamicds.model.TestRecord;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
public interface TestRecordService {

    void addTestRecord(String recordId, String recordData);

    TestRecord queryTestRecordById(String id);

    TestRecord queryTestRecordByRecordId(String recordId);

}
