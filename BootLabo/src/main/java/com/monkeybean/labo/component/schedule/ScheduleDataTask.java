package com.monkeybean.labo.component.schedule;

import com.monkeybean.labo.predefine.CacheData;
import com.monkeybean.labo.service.database.LaboDoService;
import com.monkeybean.labo.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * <p>
 * Created by MonkeyBean on 2018/5/26.
 */
@Component
@EnableScheduling
public class ScheduleDataTask {

    private static Logger logger = LoggerFactory.getLogger(ScheduleDataTask.class);

    @Autowired
    private LaboDoService laboDoService;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

    // test, 每分钟执行一次
    // @Scheduled(cron = "0 */1 * * * ?")

    /**
     * 每日凌晨四点清理数据库无用数据及缓存
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void dataCleaner() {
        logger.info("clean old data");

        //清除数据库中一月前的临时图片
        laboDoService.clearTempAssetBeforeDate(DateUtil.getOneMonthAgo());

        //清除邮件激活缓存key
        CacheData.getMailKeyMap().clear();

        //接口请求次数置空
        CacheData.getRequestCountMap().clear();

        //邮件发送次数清空
        CacheData.getMailSendNumMap().clear();
    }

}
