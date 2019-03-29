package com.jay.vito.uic.client.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户认证拦截器
 *
 * @author zhaixm
 */
public class BaseAuthInterceptor extends HandlerInterceptorAdapter {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 是否忽略用户认证
     *
     * @param request
     * @param handlerMethod
     * @return
     */
    protected boolean isIgnore(HttpServletRequest request, HandlerMethod handlerMethod) {
        // options方法不拦截
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 有配置该注解，不进行认证拦截
        IgnoreUserAuth ignoreAnnotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserAuth.class);
        if (ignoreAnnotation == null) {
            ignoreAnnotation = handlerMethod.getMethodAnnotation(IgnoreUserAuth.class);
        }
        if (ignoreAnnotation == null) {
            return false;
        } else {
            return true;
        }
    }
}
