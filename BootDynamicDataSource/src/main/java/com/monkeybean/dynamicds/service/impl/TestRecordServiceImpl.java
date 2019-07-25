package com.monkeybean.dynamicds.service.impl;

import com.monkeybean.dynamicds.dao.TestRecordDao;
import com.monkeybean.dynamicds.model.TestRecord;
import com.monkeybean.dynamicds.service.TestRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 开启事务后, 数据源切换失效, 因为determineCurrentLookupKey()在aop拦截之前执行, 采用如下方式解决
 * 1.可通过在业务层而不是数据层加拦截, 如可考虑在controller中加aop拦截
 * 2.propagation设为SUPPORTS即可(推荐), 此时determineCurrentLookupKey()在aop拦截之后执行
 * <p>
 * Created by MonkeyBean on 2019/7/21.
 */
@Transactional(propagation = Propagation.SUPPORTS)
@Service
public class TestRecordServiceImpl implements TestRecordService {

    @Resource
    private TestRecordDao testRecordDao;

    @Override
    public void addTestRecord(String recordId, String recordData) {
        TestRecord testRecord = new TestRecord();
        testRecord.setRecordId(recordId);
        testRecord.setRecordData(recordData);
        testRecordDao.addTestRecord(testRecord);
    }

    @Override
    public TestRecord queryTestRecordById(String id) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        List<TestRecord> recordList = testRecordDao.queryTestRecord(paramMap);
        if (!recordList.isEmpty()) {
            return recordList.get(0);
        }
        return null;
    }

    @Override
    public TestRecord queryTestRecordByRecordId(String recordId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("recordId", recordId);
        List<TestRecord> recordList = testRecordDao.queryTestRecord(paramMap);
        if (!recordList.isEmpty()) {
            return recordList.get(0);
        }
        return null;
    }
}
