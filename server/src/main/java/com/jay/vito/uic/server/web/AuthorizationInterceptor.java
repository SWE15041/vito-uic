package com.jay.vito.uic.server.web;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者: zhaixm
 * 日期: 2017/12/12 11:52
 * 描述: 可以看看UserRoleAuthorizationInterceptor如何用
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception {
        return false;
    }

}
