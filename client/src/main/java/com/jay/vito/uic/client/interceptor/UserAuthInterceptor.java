package com.jay.vito.uic.client.interceptor;

import com.jay.vito.common.exception.HttpUnauthorizedException;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContext;
import com.jay.vito.uic.client.core.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.jay.vito.uic.client.core.TokenUtil.getToken;
import static com.jay.vito.uic.client.core.TokenUtil.parseToken;

/**
 * 用户认证拦截器
 *
 * @author zhaixm
 */
public class UserAuthInterceptor extends HandlerInterceptorAdapter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String uicDomain;
	protected String appDomain;

	/**
	 * 是否忽略用户认证
	 *
	 * @param request
	 * @param handlerMethod
	 * @return
	 */
	protected boolean isIgnore(HttpServletRequest request, HandlerMethod handlerMethod) {
		// options方法不拦截
		if ("OPTIONS".equals(request.getMethod())) {
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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 配置该注解，说明不进行用户拦截
			if (!isIgnore(request, handlerMethod)) {
				String token = getToken(request);
				if (Validator.isNotNull(token)) {
					try {
						UserContext userContext = parseToken(token, uicDomain, appDomain);
						UserContextHolder.setUserContext(userContext);
					} catch (Exception e) {
						logger.error("token解析失败", e);
						throw new HttpUnauthorizedException("token解析失败");
					}
				} else {
					throw new HttpUnauthorizedException("token解析失败");
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContextHolder.clearUserContext();
		super.afterCompletion(request, response, handler, ex);
	}
}
