package com.vito.uic.client.core;


import com.vito.common.util.validate.Validator;

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
    public static UserContext getUserContext() {
        return userContextLocal.get();
    }

    public static Long getCurrentUserId() {
        if (Validator.isNotNull(getUserContext())) {
            return getUserContext().getUserId();
        }
        return null;
    }

    public static Long getCurrentGroupId() {
        if (Validator.isNotNull(getUserContext())) {
            return getUserContext().getGroupId();
        }
        return null;
    }

    public static void setUserContext(TokenData tokenData) {
        userContextLocal.set(new UserContext(tokenData));
    }

}
