package com.monkeybean.mvc.service.impl;

import com.monkeybean.mvc.entity.TestRecord;
import com.monkeybean.mvc.mapper.TestRecordMapper;
import com.monkeybean.mvc.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MonkeyBean on 2018/11/19.
 */
@Service
public class TestServiceImpl implements TestService {

    private final TestRecordMapper testRecordMapper;

    @Autowired
    public TestServiceImpl(TestRecordMapper testRecordMapper) {
        this.testRecordMapper = testRecordMapper;
    }

    @Override
    public void saveTestRecord(TestRecord testRecord) {
        testRecordMapper.saveTestRecord(testRecord);
    }
}
