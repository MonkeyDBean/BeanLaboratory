package com.monkeybean.labo.component.filter;

import com.alibaba.fastjson.JSON;
import com.monkeybean.labo.component.reqres.Result;
import com.monkeybean.labo.predefine.ReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * 统一异常处理
 * <p>
 * Created by MonkeyBean on 2019/10/15.
 */
@Configuration
public class ExceptionConfigurer implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(ExceptionConfigurer.class);

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            Result result;
            if (e instanceof BindException) {
                result = new Result<>(ReturnCode.BAD_REQUEST, getDataFromFieldError(((BindException) e).getFieldError()));
            } else if (e instanceof MethodArgumentNotValidException) {
                result = new Result<>(ReturnCode.BAD_REQUEST, getDataFromFieldError(((MethodArgumentNotValidException) e).getBindingResult().getFieldError()));
            } else if (e instanceof ServletException) {
                result = new Result<>(ReturnCode.BAD_REQUEST, e.getMessage());
            } else {
                result = new Result<>(ReturnCode.INTERNAL_SERVER_ERROR, e.getMessage());
            }

            // 根据错误原因区分日志级别
            if (result.getCode() == ReturnCode.BAD_REQUEST.getCode()) {
                logger.warn("bad request, error: [{}], requestUri: [{}]", e, request.getRequestURI());
            } else {
                logger.error("error happen, error: [{}], requestUri: [{}]", e, request.getRequestURI());
            }
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.setStatus(200);
            try {
                response.getWriter().write(JSON.toJSONString(result));
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
            return new ModelAndView();
        });
    }

    /**
     * 获取错误描述
     */
    private String getDataFromFieldError(FieldError fieldError) {
        if (fieldError != null) {
            String message = fieldError.getDefaultMessage();
            if (!StringUtils.isEmpty(message)) {
                return message;
            }
        }
        return ReturnCode.BAD_REQUEST.getDescription();
    }
}
