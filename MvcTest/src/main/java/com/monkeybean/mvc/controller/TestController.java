package com.monkeybean.mvc.controller;

import com.monkeybean.mvc.entity.TestRecord;
import com.monkeybean.mvc.service.impl.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MonkeyBean on 2018/11/19.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private final TestServiceImpl testService;

    @Value("${TEST_ACCESS_URL}")
    private String testAccessUrl;

    @Autowired
    public TestController(TestServiceImpl testService) {
        this.testService = testService;
    }

    @RequestMapping("/config")
    public List<String> getConfig() {
        List<String> data = new ArrayList<>();
        data.add(testAccessUrl);
        return data;
    }

    //http://localhost:8080/test/data/save?testData=test_one
    @RequestMapping("/data/save")
    public String saveData(String testData) {
        if (!StringUtils.hasLength(testData) || testData.length() >= 50) {
            logger.warn("param is illegal: {}", testData);
            return "param illegal";
        }
        TestRecord testRecord = new TestRecord();
        testRecord.setRecordData(testData);
        testRecord.setRecordId(UUID.randomUUID().toString());
        testService.saveTestRecord(testRecord);
        return "success";
    }

}
