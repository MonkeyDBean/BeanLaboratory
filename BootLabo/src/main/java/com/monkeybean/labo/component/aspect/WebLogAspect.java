package com.monkeybean.labo.component.aspect;

import com.monkeybean.labo.component.config.OtherConfig;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final OtherConfig otherConfig;
    private ThreadLocal<Long> beginTime = new ThreadLocal<>();
    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Autowired
    public WebLogAspect(OtherConfig otherConfig) {
        this.otherConfig = otherConfig;
    }

    /**
     * 监听所有controller的所有公共方法
     */
    @Pointcut("execution(public * com.monkeybean.labo.controller.*.*(..))")
    public void controllerPoint() {
    }

    /**
     * 监听GeneralController的公共方法
     */
    @Pointcut("execution(public * com.monkeybean.labo.controller.GeneralController.*(..))")
    public void generalControllerPoint() {
    }

    @Before("controllerPoint()")
    public void doBefore(JoinPoint joinPoint) {

        //请求到达时间
        beginTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //日志过多，嗅探接口和健康检查接口, 不打印请求记录
        String requestUrl = request.getRequestURL().toString();
        if (!requestUrl.contains("sniff/status") && !requestUrl.contains("monitor/actuator")) {
            String requestParam = Arrays.toString(joinPoint.getArgs());
            logger.info("Request Url: [{}]", request.getRequestURL());
            logger.info("Args: [{}]", requestParam);
            logger.info("Http Method: [{}]", request.getMethod());
            logger.info("Class Method: [{}].[{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        }
    }

    @AfterReturning(returning = "ret", pointcut = "controllerPoint()")
    public void doAfterReturning(Object ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURL().toString();
        if (!requestUrl.contains("sniff/status") && !requestUrl.contains("monitor/actuator")) {
            logger.info("Response is: [{}]", ret);
            if (ret instanceof Result) {
                Result result = (Result) ret;
                logger.info("Response code is [{}]", result.getCode());
                logger.info("Response message is [{}]", result.getMsg());
                if (result.getCode() != ReturnCode.SUCCESS.getCode()) {
                    logger.warn("request: [{}], return code is not success: [{}]", request.getRequestURI(), result.getCode());
                }
            }
            logger.info("method execute takes: [{}] ms", System.currentTimeMillis() - beginTime.get());
        }
        beginTime.remove();
    }

    /**
     * 环绕通知
     */
    @Around("generalControllerPoint()")
    public Object around(ProceedingJoinPoint pjp) {
        logger.info("method around start.....");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestPath = request.getServletPath();
        String queryStr = request.getQueryString();
        HttpServletResponse response = attributes.getResponse();
        boolean validOk = true;
        if (requestPath.contains("general/config/reload")) {
            if (queryStr == null || !queryStr.toLowerCase().contains(otherConfig.getReloadKey())) {
                logger.warn("config reload, key is null or wrong");
                validOk = false;
            }
        }
        if (validOk) {
            try {
                Object o = pjp.proceed();
                logger.info("method around proceed，result is: [{}]", o);
                return o;
            } catch (Throwable t) {
                logger.error("@Around get throwable: [{}]", t);
                return new Result<>(ReturnCode.SERVER_EXCEPTION);
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new Result<>(ReturnCode.PARAM_ILLEGAL);
        }
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

}
