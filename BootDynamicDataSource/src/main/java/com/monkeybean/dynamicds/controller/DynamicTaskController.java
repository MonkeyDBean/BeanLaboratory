package com.monkeybean.dynamicds.controller;

import com.monkeybean.dynamicds.component.schedule.ScheduleTaskJob;
import com.monkeybean.dynamicds.constant.ConstantValue;
import com.monkeybean.dynamicds.reqres.JobReq;
import com.monkeybean.dynamicds.reqres.Result;
import com.monkeybean.dynamicds.reqres.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * 动态定时任务测试
 * 1.ThreadPoolTaskScheduler为线程池任务调度类, 能够开启线程池进行任务调度
 * 2.ThreadPoolTaskScheduler.schedule()会创建一个定时计划ScheduledFuture, 在这个方法包含两个参数: Runnable和CronTrigger(定时任务触发器)
 * 3.ScheduledFuture中cancel方法可停止定时任务
 * <p>
 * Created by MonkeyBean on 2019/7/21.
 */
@Api(value = "动态定时任务测试接口")
@RequestMapping(path = "test/schedule")
@RestController
@Slf4j
public class DynamicTaskController {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    public DynamicTaskController(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    @ApiOperation(value = "获取定时任务标记列表")
    @GetMapping("job/key/query")
    public Result<Set<String>> queryJobKeys() {
        return new Result<>(ReturnCode.SUCCESS, ScheduleTaskJob.getScheduledFutureMap().keySet());
    }

    @ApiOperation(value = "删除特定定时任务")
    @GetMapping("job/remove")
    public Result<String> removeJob(@RequestParam String name) {
        ScheduledFuture job = ScheduleTaskJob.getScheduledFutureMap().get(name);
        if (job != null) {
            boolean cancelResult = job.cancel(false);
            if (cancelResult) {
                ScheduleTaskJob.getScheduledFutureMap().remove(name);
                return new Result<>(ReturnCode.SUCCESS, ConstantValue.SUCCESS_FLAG);
            } else {
                return new Result<>(ReturnCode.SUCCESS, ConstantValue.FAIL_FLAG);
            }
        }
        return new Result<>(ReturnCode.BAD_REQUEST);
    }

    @ApiOperation(value = "添加定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "任务名称", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cron", value = "cron表达式", required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("job/add")
    public Result<String> addJob(JobReq reqModel) {
        if (!CronExpression.isValidExpression(reqModel.getCron()) || ScheduleTaskJob.getScheduledFutureMap().get(reqModel.getName()) != null) {
            return new Result<>(ReturnCode.BAD_REQUEST);
        }
        ScheduledFuture job = threadPoolTaskScheduler.schedule(() -> log.info("[{}] is running, currentThread is: [{}]", reqModel.getName(), Thread.currentThread().getName()), new CronTrigger(reqModel.getCron()));
        ScheduleTaskJob.getScheduledFutureMap().put(reqModel.getName(), job);
        return new Result<>(ReturnCode.SUCCESS, ConstantValue.SUCCESS_FLAG);
    }

}
