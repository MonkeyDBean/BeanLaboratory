package com.monkeybean.schedule.component;

import com.monkeybean.schedule.dao.ScheduleDataDao;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Quartz job
 * <p>
 * Created by MonkeyBean on 2019/1/23.
 */
@PersistJobDataAfterExecution //持久化
@DisallowConcurrentExecution //禁止并发执行(Quartz不要并发地执行同一个job定义(这里指一个job类的多个实例))
public class MyQuartzJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(MyQuartzJob.class);
    private ScheduleDataDao scheduleDataDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String taskName = context.getJobDetail().getJobDataMap().getString("name");
        String triggerKey = context.getTrigger().getKey().toString();
        logger.info("....MyQuartzJob execute, triggerKey: {}, taskName: {}, time: {}....", triggerKey, taskName, System.currentTimeMillis());
        try {
            this.scheduleDataDao = (ScheduleDataDao) context.getScheduler().getContext().get("scheduleDataDao");
            Map<String, Object> param = new HashMap<>();
            param.put("taskId", triggerKey);
            param.put("uuid", UUID.randomUUID().toString());
            param.put("custom", "quartz job, insert operation");
            this.scheduleDataDao.addActualTaskRecord(param);
        } catch (SchedulerException e) {
            logger.error("JobExecutionContext getScheduler error: {}", e);
        }
    }
}
