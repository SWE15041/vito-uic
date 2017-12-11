package com.vito.uic.client;

import com.vito.common.util.json.JsonParser;
import com.vito.common.util.validate.Validator;
import com.vito.uic.client.core.TokenData;
import com.vito.uic.client.core.TokenUtil;
import com.vito.uic.client.core.UserContextHolder;
import com.vito.uic.client.model.ApiErrorResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:07
 * 描述: 认证过滤器
 */
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    protected String[] excludePaths;

    protected String uicDomain;
    protected String appDomain;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.uicDomain = filterConfig.getInitParameter("uicDomain");
        this.appDomain = filterConfig.getInitParameter("appDomain");
        String excludePathsStr = filterConfig.getInitParameter("excludePaths");
        if (Validator.isNotNull(excludePathsStr)) {
            this.excludePaths = excludePathsStr.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        String ctxPath = httpReq.getServletPath();
        boolean exclude = false;
        if (Validator.isNotNull(excludePaths)) {
            for (String excludePath : excludePaths) {
                if (ctxPath.startsWith(excludePath)) {
                    exclude = true;
                }
            }
        }
        if (!exclude) {
            String authorization = httpReq.getHeader("Authorization");
            if (Validator.isNotNull(authorization)) {
                String token = authorization.split(" ")[1];
                try {
                    TokenData tokenData = TokenUtil.parseToken(token, uicDomain, appDomain);
                    UserContextHolder.setUserContext(tokenData);
                } catch (Exception e) {
                    logger.error("token解析失败", e);
                    authFail(httpResp);
                    return;
                }
            } else {
                authFail(httpResp);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void authFail(HttpServletResponse httpResp) throws IOException {
        httpResp.setContentType("application/json");
        httpResp.setCharacterEncoding("UTF-8");
        httpResp.setStatus(HttpStatus.SC_FORBIDDEN);
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setMsg("token验证失败，无权访问该数据");
        errorResponse.setErrCode("INVALID_AUTH_TOKEN");
        httpResp.getWriter().write(JsonParser.convertObjectToJson(errorResponse));
    }

    @Override
    public void destroy() {

    }

    public String getUicDomain() {
        return uicDomain;
    }

    public void setUicDomain(String uicDomain) {
        this.uicDomain = uicDomain;
    }

    public String getAppDomain() {
        return appDomain;
    }

    public void setAppDomain(String appDomain) {
        this.appDomain = appDomain;
    }
}
