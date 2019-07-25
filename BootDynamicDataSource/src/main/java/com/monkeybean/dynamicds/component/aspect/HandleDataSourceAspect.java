package com.monkeybean.dynamicds.component.aspect;

import com.monkeybean.dynamicds.component.db.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Aspect
@Component
@Slf4j
public class HandleDataSourceAspect {

    @Pointcut("execution(public * com.monkeybean.dynamicds.dao.*.*(..))")
    public void daoPoint() {
    }

    @Before("daoPoint()")
    public void doBefore(JoinPoint joinPoint) {

        //写数据到主数据源, 从其他数据源读数据
        String methodName = joinPoint.getSignature().getName().toLowerCase();
        if (methodName.contains("select") || methodName.contains("query")) {
            DynamicDataSource.setSlaveAsDataSource();
        } else {
            DynamicDataSource.setMasterAsDataSource();
        }
        log.info("DaoMethod is: [{}], currentDataSource is: [{}]", methodName, DynamicDataSource.getCurrentDataSourceKey());
    }
}
