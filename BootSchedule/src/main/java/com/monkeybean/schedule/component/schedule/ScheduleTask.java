package com.monkeybean.schedule.component.schedule;

import com.monkeybean.schedule.dao.ScheduleDataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 测试集群定时任务，多节点仅某一节点执行定时业务处理
 * <p>
 * Created by MonkeyBean on 2019/1/23.
 */
@Component
@EnableScheduling
public class ScheduleTask {
    private static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
    private final ScheduleDataDao scheduleDataDao;

    @Autowired
    public ScheduleTask(ScheduleDataDao scheduleDataDao) {
        this.scheduleDataDao = scheduleDataDao;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    /**
     * 每3分钟执行一次
     * 以下cron表达式同fixedRate效果相同, 均为每隔3分钟执行一次，不论业务执行花费时间
     * fixedDelay为当前定时任务执行完成后, 再隔3分钟执行一次
     */
//    @Scheduled(cron = "0 */3 * * * ?")
//    @Scheduled(fixedDelay = 3000)
    @Scheduled(fixedRate = 3000)
    public void testSchedule() throws InterruptedException {
        String testTaskFlag = "testTaskId";
        logger.info("start schedule, time: {}", System.currentTimeMillis());

        //生成节点标识
        String uuid = UUID.randomUUID().toString();

        //任务记录入库
        Map<String, Object> param = new HashMap<>();
        param.put("taskId", testTaskFlag);
        param.put("uuid", uuid);
        scheduleDataDao.addTaskRecord(param);

        //休眠
        Thread.sleep(1000);

        //有效节点判断
        String validUuid = scheduleDataDao.queryLatestTaskRecord(testTaskFlag);
        if (validUuid != null && uuid.equals(validUuid)) {

            //业务处理流程TODO、执行记录入库
            param.put("custom", "valid uuid, insert operation");
            scheduleDataDao.addActualTaskRecord(param);
            logger.info("schedule execute finished, time: {}", System.currentTimeMillis());
        }
        logger.info("end schedule, time: {}", System.currentTimeMillis());
    }
}
