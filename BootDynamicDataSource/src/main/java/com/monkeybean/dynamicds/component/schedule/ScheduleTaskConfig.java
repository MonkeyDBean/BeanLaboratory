package com.monkeybean.dynamicds.component.schedule;

import com.monkeybean.dynamicds.component.PublicConfig;
import com.monkeybean.dynamicds.service.impl.PublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Configuration
@Slf4j
public class ScheduleTaskConfig {

    private final PublicConfig publicConfig;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final PublicService publicService;

    @Autowired
    public ScheduleTaskConfig(PublicConfig publicConfig, ThreadPoolTaskScheduler threadPoolTaskScheduler, PublicService publicService) {
        this.publicConfig = publicConfig;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.publicService = publicService;
    }

    /**
     * 初始添加测试定时任务
     */
    @PostConstruct
    private void testJobInit() {
        final String taskFlag = "preConfigJob";
        ScheduledFuture initSchedule = threadPoolTaskScheduler.schedule(() -> {
            String curThreadName = Thread.currentThread().getName();
            publicService.generateTestRecord(taskFlag + "_" + curThreadName);
            log.info("[{}] is running, currentThread is: [{}]", taskFlag, curThreadName);
        }, triggerContext -> new CronTrigger(publicConfig.getTestCron()).nextExecutionTime(triggerContext));
        ScheduleTaskJob.getScheduledFutureMap().put(taskFlag, initSchedule);
    }

}
