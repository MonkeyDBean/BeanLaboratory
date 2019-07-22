package com.monkeybean.labo.dao.third;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by MonkeyBean on 2018/05/26.
 */
@Mapper
public interface ThirdDataDao {
    Map<String, Object> queryRecordInfo(Map<String, Object> param);
}
