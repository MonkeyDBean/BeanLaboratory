package com.monkeybean.labo.dao.secondary;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@Mapper
public interface SecondaryDataDao {
    Map<String, Object> queryTestRecord(Map<String, Object> param);

    Map<String, Object> queryShortLongRecord(Map<String, Object> param);

    void addShortLongRecord(Map<String, Object> param);
}
