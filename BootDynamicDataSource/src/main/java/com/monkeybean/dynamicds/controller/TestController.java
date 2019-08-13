package com.monkeybean.dynamicds.controller;

import com.monkeybean.dynamicds.component.db.DynamicDataSource;
import com.monkeybean.dynamicds.constant.ConstantValue;
import com.monkeybean.dynamicds.model.TestRecord;
import com.monkeybean.dynamicds.reqres.Result;
import com.monkeybean.dynamicds.reqres.ReturnCode;
import com.monkeybean.dynamicds.service.ProfileService;
import com.monkeybean.dynamicds.service.TestRecordService;
import com.monkeybean.dynamicds.service.impl.PublicService;
import com.monkeybean.dynamicds.service.impl.TestAsyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Api(value = "简单数据接口测试")
@RequestMapping(path = "test/simple")
@RestController
@Slf4j
public class TestController {

    private final TestRecordService testRecordService;

    private final PublicService publicService;

    private final TestAsyncService testAsyncService;

    private final ProfileService profileService;

    @Autowired
    public TestController(TestRecordService testRecordService, PublicService publicService, TestAsyncService testAsyncService, ProfileService profileService) {
        this.testRecordService = testRecordService;
        this.publicService = publicService;
        this.testAsyncService = testAsyncService;
        this.profileService = profileService;
    }

    @ApiOperation(value = "新增测试记录")
    @PostMapping("record/data/add")
    public Result<String> addRecordData() {
        publicService.generateTestRecord();
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

    @ApiOperation(value = "测试@Async注解")
    @GetMapping("async/call")
    public Long[] callAsync() throws InterruptedException, TimeoutException, ExecutionException {

        //同步
        log.info("--------start-synchronous-method-test------------");
        testAsyncService.synMethod3();
        long curTimeStamp = System.currentTimeMillis();
        long synchronousCalculate = testAsyncService.synMethod1() + testAsyncService.synMethod2();
        long synchronousMethodCallTime = System.currentTimeMillis() - curTimeStamp;

        //异步
        log.info("--------start-asynchronous-method-test------------");
        testAsyncService.asynchronousMethod3();
        curTimeStamp = System.currentTimeMillis();
        Future<Long> asynchronousMethod1Future = testAsyncService.asynchronousMethod1();
        Future<Long> asynchronousMethod2Future = testAsyncService.asynchronousMethod2();
        long asynchronousMethod1Res = asynchronousMethod1Future.get(5, TimeUnit.SECONDS);
        long asynchronousMethod2Res = asynchronousMethod2Future.get(5, TimeUnit.SECONDS);
        long asynchronousCalculate = asynchronousMethod1Res + asynchronousMethod2Res;
        long asynchronousMethodCallTime = System.currentTimeMillis() - curTimeStamp;
        Long[] res = new Long[4];
        res[0] = synchronousMethodCallTime;
        res[1] = synchronousCalculate;
        res[2] = asynchronousMethodCallTime;
        res[3] = asynchronousCalculate;
        return res;
    }

    @ApiOperation(value = "测试@Profile注解")
    @GetMapping("profile")
    public String testProfile() {
        return profileService.getProfileInfo();
    }

}
