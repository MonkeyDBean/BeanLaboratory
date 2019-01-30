package com.monkeybean.schedule.component;

import com.monkeybean.schedule.dao.ScheduleDataDao;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Quartz定时任务
 * <p>
 * Created by MonkeyBean on 2019/1/23.
 */
@Component
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    private static String TRIGGER_KEY_NAME = "test_trigger_1";
    private static String TRIGGER_GROUP_NAME = "test_trigger_group_1";
    private static String JOB_GROUP_ID = "test_job_id_1";
    private static String JOB_GROUP_NAME = "test_job_group_1";
    private volatile AtomicBoolean isInit = new AtomicBoolean(false);
    private Logger logger = LoggerFactory.getLogger(StartApplicationListener.class);
    @Autowired
    private SchedulerConfig schedulerConfig;
    @Autowired
    private ScheduleDataDao scheduleDataDao;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //防重复触发
        if (!isInit.compareAndSet(false, true)) {
            return;
        }
        addJob();
        start();
    }

    /**
     * 调度启动
     */
    private void start() {
        try {
            Scheduler scheduler = schedulerConfig.scheduler();
            scheduler.getContext().put("scheduleDataDao", scheduleDataDao);
            scheduler.start();
            logger.info("Scheduler start, time: {}", System.currentTimeMillis());
            logger.info("Scheduler start, triggerName: {}, triggerGroup: {}, state: {}", TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME, scheduler.getTriggerState(TriggerKey.triggerKey(TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME)));
        } catch (Exception e) {
            logger.error("scheduler start error: {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 添加定时任务
     */
    private void addJob() {
        try {
            Scheduler scheduler = schedulerConfig.scheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME);
//            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {

                //定义一个JobDetail,其中的定义Job类，是真正的执行逻辑所在
                JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class)
                        .withIdentity(JOB_GROUP_ID, JOB_GROUP_NAME)
                        .usingJobData("name", "testJob")
                        .build();

                //定义一个Trigger
//                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
                SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever();
                trigger = TriggerBuilder.newTrigger().withIdentity(TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME)
                        .withSchedule(scheduleBuilder)
                        .build();

                //校验job是否已存在
                if (!scheduler.checkExists(jobDetail.getKey())) {
                    scheduler.scheduleJob(jobDetail, trigger);
                    logger.info("addMyJob, Quartz创建了job:...: {}", jobDetail.getKey());
                }

                //查看触发器状态
                logger.info("addJob, triggerName: {}, triggerGroup: {}, state: {}", TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME, scheduler.getTriggerState(TriggerKey.triggerKey(TRIGGER_KEY_NAME, TRIGGER_GROUP_NAME)));
            } else {
                logger.info("addMyJob, job已存在: {}", trigger.getKey());
            }
        } catch (Exception e) {
            logger.error("scheduler addJob error: {}", ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 修改任务的触发时间
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组名
     * @param cronStr          时间设置, cron表达式字符串
     */
    private void modifyJobCron(String triggerName, String triggerGroupName, String cronStr) {
        try {
            Scheduler scheduler = schedulerConfig.scheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldCronStr = trigger.getCronExpression();
            if (!oldCronStr.equalsIgnoreCase(cronStr)) {

                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();

                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();

                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronStr));

                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();

                //修改任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            logger.error("scheduler modifyJobCron error: {}", ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName          任务名称
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组名
     */
    private void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler scheduler = schedulerConfig.scheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            // 停止触发器
            scheduler.pauseTrigger(triggerKey);

            // 移除触发器
            scheduler.unscheduleJob(triggerKey);

            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            logger.error("scheduler removeJob error: {}", ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException(e);
        }
    }

}
