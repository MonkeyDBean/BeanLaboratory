package com.monkeybean.dynamicds.controller;

import com.monkeybean.dynamicds.component.db.DynamicDataSource;
import com.monkeybean.dynamicds.constant.ConstantValue;
import com.monkeybean.dynamicds.model.TestRecord;
import com.monkeybean.dynamicds.reqres.Result;
import com.monkeybean.dynamicds.reqres.ReturnCode;
import com.monkeybean.dynamicds.service.TestRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Api(value = "测试接口")
@RequestMapping(path = "test")
@RestController
public class TestController {

    private final TestRecordService testRecordService;

    @Autowired
    public TestController(TestRecordService testRecordService) {
        this.testRecordService = testRecordService;
    }

    @ApiOperation(value = "新增测试记录")
    @PostMapping("record/data/add")
    public Result<String> addRecordData() {
        String recordId = UUID.randomUUID().toString();
        String recordData = "Test_" + recordId.substring(0, 8);
        testRecordService.addTestRecord(recordId, recordData);
        return new Result<>(ReturnCode.SUCCESS, ConstantValue.SUCCESS_FLAG);
    }

    @ApiOperation(value = "通过Id查询特定记录")
    @GetMapping("record/data/query")
    public Result<TestRecord> queryRecordDataById(@RequestParam String Id) {
        return new Result<>(ReturnCode.SUCCESS, testRecordService.queryTestRecordById(Id));
    }

    @ApiOperation(value = "通过记录Id批量查询")
    @GetMapping("record/data/s/query")
    public Result<List<TestRecord>> queryRecordDataByRecordIds(@RequestParam String... recordId) {
        List<TestRecord> data = new ArrayList<>();
        for (String each : recordId) {
            TestRecord record = testRecordService.queryTestRecordById(each);
            if (record != null) {
                data.add(record);
            }
        }
        return new Result<>(ReturnCode.SUCCESS, data);
    }

    @ApiOperation(value = "获取所有数据源标记")
    @GetMapping("source/data/key/query")
    public Result<List<String>> queryDataSourceKeys() {
        return new Result<>(ReturnCode.SUCCESS, DynamicDataSource.getDataSourceKeys());
    }
}