package com.vito.uic.controller;

import com.vito.common.model.Pair;
import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.uic.domain.User;
import com.vito.website.constant.SessionConstant;
import org.hibernate.Session;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:07
 * 描述:
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        String redirectTarget = httpReq.getParameter("target");
        //todo 获取全部注册过的app domain 验证redirectTarget是否与app domain匹配

    }

    @Override
    public void destroy() {

    }
}
