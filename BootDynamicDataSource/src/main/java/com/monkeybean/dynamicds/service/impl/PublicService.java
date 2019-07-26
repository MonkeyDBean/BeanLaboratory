package com.monkeybean.dynamicds.service.impl;

import com.monkeybean.dynamicds.service.TestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Service
public class PublicService {

    private final TestRecordService testRecordService;

    @Autowired
    public PublicService(TestRecordService testRecordService) {
        this.testRecordService = testRecordService;
    }

    /**
     * 生成测试记录
     */
    public void generateTestRecord() {
        generateTestRecord(null);
    }

    /**
     * 生成测试记录
     *
     * @param recordDataPrefix 测试数据前缀
     */
    public void generateTestRecord(String recordDataPrefix) {
        if (StringUtils.isEmpty(recordDataPrefix)) {
            recordDataPrefix = "SimpleTest";
        }
        String recordId = UUID.randomUUID().toString();
        String recordData = recordDataPrefix + "_" + recordId.substring(0, 8);
        testRecordService.addTestRecord(recordId, recordData);
    }
}
