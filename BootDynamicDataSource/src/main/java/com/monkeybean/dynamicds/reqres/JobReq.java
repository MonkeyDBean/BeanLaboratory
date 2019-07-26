package com.monkeybean.dynamicds.reqres;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Getter
@Setter
public class JobReq {
    /**
     * 定时任务名称
     */
    @NotEmpty
    private String name;

    /**
     * cron表达式
     */
    @NotEmpty
    private String cron;
}
