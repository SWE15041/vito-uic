package com.jay.vito.uic.client.interceptor;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.website.core.exception.HttpUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.jay.vito.uic.client.core.TokenUtil.*;

public class UserAuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    protected String uicDomain;
    protected String appDomain;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // options方法不拦截
        if ("OPTIONS".equals(request.getMethod())) {
            return super.preHandle(request, response, handler);

        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行用户拦截
        IgnoreUserAuth ignoreAnnotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserAuth.class);
        if (ignoreAnnotation == null) {
            ignoreAnnotation = handlerMethod.getMethodAnnotation(IgnoreUserAuth.class);
        }
        if (ignoreAnnotation != null) {
            return super.preHandle(request, response, handler);
        } else {
            String token = getToken(request);
            if (Validator.isNotNull(token)) {
                try {
                    TokenData tokenData = parseToken(token, uicDomain, appDomain);
                    UserContextHolder.setUserContext(tokenData);
                } catch (Exception e) {
                    logger.error("token解析失败", e);
                    authFail(request, response);
                    throw new HttpUnauthorizedException("token解析失败");
                }
            } else {
                throw new HttpUnauthorizedException("token解析失败");
            }
            return super.preHandle(request, response, handler);
        }
    }


}
