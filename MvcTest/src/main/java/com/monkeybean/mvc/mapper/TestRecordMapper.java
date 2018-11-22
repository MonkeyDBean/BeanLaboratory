package com.monkeybean.mvc.mapper;

import com.monkeybean.mvc.entity.TestRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by MonkeyBean on 2018/11/19.
 */
@Repository
public interface TestRecordMapper {
    void saveTestRecord(TestRecord testRecord);
}
