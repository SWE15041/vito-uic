package com.vito.uic.web;

import com.vito.common.util.string.EncodeUtil;
import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.uic.core.AppDataCache;
import com.vito.uic.domain.RegApp;
import com.vito.uic.domain.User;
import com.vito.website.constant.SessionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:07
 * 描述: 认证过滤器
 */
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private String[] excludePath = {"/auth", "/login.html", "/app-invalid.html"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        String ctxPath = httpReq.getServletPath();
        logger.debug("当前请求路径为：{}", ctxPath);
        if (ctxPath.startsWith("auth")) {
            String reqIp = WebUtil.getIpAddr(httpReq);
            if (!validReqIp(reqIp)) {
                httpResp.sendRedirect("/invalid.html");
            }
        }
        String encodeTargetUrl = httpReq.getParameter("target");
        if (Validator.isNotNull(encodeTargetUrl)) {
            String targetUrl = EncodeUtil.decodeStr(encodeTargetUrl, EncodeUtil.UTF_8);
            // 获取全部注册过的app domain 验证redirectTarget是否与app domain匹配
            if (!validTargetUrl(targetUrl)) {
                // 未通过app合法性验证转向错误页面
                httpResp.sendRedirect("/invalid.html");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void validSessionUser(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        User sessionUser = (User) httpReq.getSession().getAttribute(SessionConstant.USER);
        if (Validator.isNotNull(sessionUser)) {
            httpResp.sendRedirect("/index.html");
        } else {
            httpResp.sendRedirect("/login.html");
        }
    }

    private boolean validTargetUrl(String targetUrl) {
        List<RegApp> regApps = AppDataCache.getEnableRegApps();
        boolean valid = false;
        for (RegApp regApp : regApps) {
            if (targetUrl.indexOf(regApp.getDomain()) > -1) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    private boolean validReqIp(String reqIp) {
        List<RegApp> regApps = AppDataCache.getEnableRegApps();
        boolean valid = true;
        for (RegApp regApp : regApps) {
            String whiteIps = regApp.getWhiteIps();
            if (Validator.isNotNull(whiteIps) && Arrays.binarySearch(whiteIps.split(","), reqIp) < 0) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    @Override
    public void destroy() {

    }
}
