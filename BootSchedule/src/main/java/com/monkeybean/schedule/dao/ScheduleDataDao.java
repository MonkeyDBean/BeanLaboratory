package com.monkeybean.schedule.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Created by MonkeyBean on 2019/1/23.
 */
@Mapper
public interface ScheduleDataDao {

    /**
     * task_record表, select
     */
    String queryLatestTaskRecord(String taskId);

    /**
     * task_record表, insert
     */
    void addTaskRecord(Map<String, Object> param);

    /**
     * actual_task_record表, insert
     */
    void addActualTaskRecord(Map<String, Object> param);

}
