package com.jay.vito.uic.client.core;


/**
 * 作者: zhaixm
 * 日期: 2017/12/3 23:29
 * 描述: 用户上下文工具
 */
public class UserContextHolder {

	private static ThreadLocal<UserContext> userContextLocal = new ThreadLocal<>();

	/**
	 * 获取当前上下文中的用户
	 *
	 * @return
	 */
	public static UserContext getContext() {
		UserContext userContext = userContextLocal.get();
		if (userContext == null) {
			userContext = new UserContext();
			userContextLocal.set(userContext);
		}
		return userContext;
	}

	public static Long getCurrentUserId() {
		return getContext().getUserId();
	}

	public static Long getCurrentGroupId() {
		return getContext().getGroupId();
	}

	public static void setUserContext(TokenData tokenData) {
		userContextLocal.set(new UserContext(tokenData));
	}

	public static void clearUserContext() {
		userContextLocal.remove();
	}

}
