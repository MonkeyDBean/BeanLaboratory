package com.monkeybean.labo.component.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 请求参数预处理
 * <p>
 * Created by MonkeyBean on 2019/8/31.
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 过滤parameterMap中的参数, 避免出现参数值为null或undefined导致参数转换失败的情况
     *
     * @param name 参数名
     */
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (StringUtils.isEmpty(value) || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined")) {
                    values[i] = null;
                }
            }
            return values;
        }
        return null;
    }

}
