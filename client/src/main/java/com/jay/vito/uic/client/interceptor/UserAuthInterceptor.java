package com.jay.vito.uic.client.interceptor;

import com.jay.vito.common.exception.HttpUnauthorizedException;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContext;
import com.jay.vito.uic.client.core.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.jay.vito.uic.client.core.TokenUtil.getToken;
import static com.jay.vito.uic.client.core.TokenUtil.parseToken;

/**
 * 用户认证拦截器
 *
 * @author zhaixm
 */
public class UserAuthInterceptor extends BaseAuthInterceptor {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String uicDomain;
	protected String appDomain;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			// 配置该注解，说明不进行用户拦截
			if (isIgnore(request, handlerMethod)) {
				return true;
			}
			String token = getToken(request);
			if (Validator.isNotNull(token)) {
				try {
					UserContext userContext = parseToken(token, uicDomain, appDomain);
					UserContextHolder.setUserContext(userContext);
					return true;
				} catch (Exception e) {
					logger.error("token解析失败", e);
					throw new HttpUnauthorizedException("token解析失败");
				}
			} else {
				throw new HttpUnauthorizedException("token解析失败");
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
