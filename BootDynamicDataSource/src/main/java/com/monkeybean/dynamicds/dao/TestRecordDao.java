package com.monkeybean.dynamicds.dao;

import com.monkeybean.dynamicds.model.TestRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Mapper
public interface TestRecordDao {

    void addTestRecord(TestRecord testRecord);

    List<TestRecord> queryTestRecord(Map<String, Object> param);

}
