package com.monkeybean.labo.component.aspect;

import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 请求日志
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Aspect
@Component
public class WebLogAspect {

    private static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    private ThreadLocal<Long> beginTime = new ThreadLocal<>();

    /**
     * 监听所有controller的所有公共方法
     */
    @Pointcut("execution(public * com.monkeybean.labo.controller.*.*(..))")
    public void controllerPoint() {
    }

    @Before("controllerPoint()")
    public void doBefore(JoinPoint joinPoint) {

        //请求到达时间
        beginTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("Request Url: {}", request.getRequestURL());
        logger.info("Http Method: {}", request.getMethod());
        logger.info("Class Method: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("Args: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret", pointcut = "controllerPoint()")
    public void doAfterReturning(Object ret) {
        logger.info("Response is: {}", ret);
        if (ret instanceof Result) {
            Result result = (Result) ret;
            logger.info("Response code is {}", result.getCode());
            logger.info("Response message is {}", result.getMsg());
            if (result.getCode() != ReturnCode.SUCCESS.getCode()) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                logger.warn("request: {}, return code is not success: {}", request.getRequestURI(), result.getCode());
            }
        }
        logger.info("method execute takes: {} ms", System.currentTimeMillis() - beginTime.get());
        beginTime.remove();
    }

//    /**
//     * 与catch完全处理异常不同，AfterThrowing不能完全处理该异常，异常依然会传播到上一级调用者直至JVM
//     */
//    @AfterThrowing(throwing = "tr", pointcut = "controllerPoint()")
//    public void throwsTr(Throwable tr) {
//        logger.error("check throwable: " + tr);
//    }
//
//    //后置最终通知, final增强，不管是抛出异常或者正常退出都会执行
//    @After("controllerPoint()")
//    public void after() {
//        logger.info("method execute finally.....");
//    }
//
//    //环绕通知
//    @Around("controllerPoint()")
//    public Object around(ProceedingJoinPoint pjp) {
//        logger.info("method around start.....");
//        try {
//            Object o = pjp.proceed();
//            logger.info("method around proceed，result is: " + o);
//            return o;
//        } catch (Throwable e) {
//            logger.error("@Around get throwable: " + e);
//            return null;
//        }
//    }

}
