package com.kodgames.agent.component.filter;

import com.kodgames.common.predefine.AgentConst;
import com.kodgames.common.util.Coder;
import com.kodgames.common.util.LegalUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by zhangbin on 2017/7/17.
 */
public class TokenKeyFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(TokenKeyFilter.class);

    /**
     * 是否开启拦截器
     */
    private boolean isFilter;

    public TokenKeyFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String servletPath = httpServletRequest.getServletPath().toLowerCase();

        //状态嗅探接口，脚本定时请求，日志单设为debug级别
        if (servletPath.contains("sniff/status")) {
            logger.debug("request param url: {}", httpServletRequest.getRequestURL());
            logger.debug("request param queryString: {}", httpServletRequest.getQueryString());
        } else {
            logger.info("request param url: {}", httpServletRequest.getRequestURL());
            logger.info("request param queryString: {}", httpServletRequest.getQueryString());
        }

        //是否开启安全校验
        if (!isFilter) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        //安全验证，签名校验
        if (servletPath.contains("swagger") || servletPath.contains("api-docs")) {
            //swaggerUI文档通过密钥进入
            if (httpServletRequest.getServletPath().toLowerCase().contains("/swagger-ui.html")) {
                if (httpServletRequest.getQueryString() == null || !httpServletRequest.getQueryString().toLowerCase().contains(AgentConst.KEY_SWAGGER)) {
                    logger.info("swagger key is wrong");
                    httpServletResponse.setStatus(401);
                    return;
                }
            }
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else if (servletPath.contains("sniff/status")) { //嗅探服务器是否正常运行
            if (httpServletRequest.getQueryString() == null || !httpServletRequest.getQueryString().toLowerCase().contains(AgentConst.KEY_SNIFF)) {
                logger.warn("sniff key is wrong");
                httpServletResponse.setStatus(401);
                return;
            }
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else if (servletPath.contains("testtest")) {  //测试接口，上线不可访问
//            chain.doFilter(httpServletRequest, httpServletResponse);
            httpServletResponse.setStatus(401);
            return;
        } else if (servletPath.contains("gmtconfig")) { //现有接口配置不走gmt，线上接口一律拦截
            String sign = request.getParameter("sign");
            String sTime = request.getParameter("stime");
            if (sign == null || sTime == null || !LegalUtil.isLegalTimestamp(sTime)) {
                logger.warn("gmt, safe param lack or illegal");
                httpServletResponse.setStatus(401);
                return;
            }
            long internalTime = Math.abs(System.currentTimeMillis() - Long.parseLong(sTime));
            if (internalTime > AgentConst.FILTER_TIMEOUT) {
                logger.warn("gmt, sTime is illegal, sTime is: {}, internalTime is: {}", sTime, internalTime);
                httpServletResponse.setStatus(401);
                return;
            }
            Enumeration<String> parameterNames = request.getParameterNames();
            String hashData = "";
            while (parameterNames.hasMoreElements()) {
                String eachParameter = parameterNames.nextElement();
                if (!eachParameter.equals("sign")) {
                    hashData += request.getParameter(eachParameter);
                }
            }
            String rightSign = Coder.md5(Coder.md5(hashData).toLowerCase() + AgentConst.KEY_GMT);
            if (!sign.equals(rightSign)) {
                logger.warn("gmt, sign is wrong, origin sign: {}, right sign: {}, return 401", sign, rightSign);
                httpServletResponse.setStatus(401);
                return;
            }
            chain.doFilter(httpServletRequest, httpServletResponse);

            httpServletResponse.setStatus(401);
            return;
        } else if (servletPath.contains("charge/callback") || servletPath.contains("wx/openid/login")
                || servletPath.contains("kapcha") || servletPath.contains(".txt") || servletPath.contains(".html")
                || servletPath.contains("code") || servletPath.contains("config/area/background/reload")
                || servletPath.contains("area/config/frontend/get") || servletPath.contains("favicon.ico")
                || servletPath.contains("sys/time/get") || servletPath.contains("agent/client/apply")
                || servletPath.contains("agent/code/recommend")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else if (servletPath.contains("agentboot")) {

            // sign=aes(md5(data+ostime), ostime)
            String osign = request.getParameter("osign");
            logger.info("parameter osign：{}", osign);
            String ostime = request.getParameter("ostime");
            if (osign == null || ostime == null || !LegalUtil.isLegalTimestamp(ostime)) {
                logger.warn("safe param lack or illegal");
                httpServletResponse.setStatus(401);
                return;
            }
            long internalTime = Math.abs(System.currentTimeMillis() - Long.parseLong(ostime));
            if (internalTime > AgentConst.FILTER_TIMEOUT) {
                logger.warn("ostime is illegal, ostime is: {}, internalTime is: {}", ostime, internalTime);
                httpServletResponse.setStatus(401);
                return;
            }
            Enumeration<String> parameterNames = request.getParameterNames();
            String hashData = "";
            while (parameterNames.hasMoreElements()) {
                String eachParameter = parameterNames.nextElement();
                if (!eachParameter.equals("osign")) {
                    hashData += request.getParameter(eachParameter);
                }
            }
            String rightSign = DigestUtils.md5Hex(hashData);
            logger.info("calculate rightSign：{}", rightSign);
            if (!osign.equalsIgnoreCase(rightSign)) {
                logger.warn("osign is wrong, return 401");
                httpServletResponse.setStatus(401);
                return;
            }

            //Session判断(是否包含agencyID)，排除以下无需验证session的请求
            if (servletPath.contains("agentstatus/list") || servletPath.contains("agent/apply") || servletPath.contains("old/login")
                    || servletPath.contains("passwd/reset") || servletPath.contains("validcode/send") || servletPath.contains("phone/valid")
                    || servletPath.contains("phone/unlock") || servletPath.contains("protocol/user/list")) {
                chain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            logger.info("filter sessionId: {}", httpServletRequest.getSession().getId());
            if (httpServletRequest.getSession().getAttribute(AgentConst.ID_FLAG_GUIYANG) == null) {
                logger.debug("agent filter 403, session agencyID is null");
                httpServletResponse.setStatus(403);
                return;
            }
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

}
