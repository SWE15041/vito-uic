package com.vito.uic.client.support;

import com.vito.common.util.validate.Validator;
import com.vito.uic.client.model.User;
import com.vito.uic.client.model.UserContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * 作者: zhaixm
 * 日期: 2017/12/3 23:29
 * 描述: 用户上下文工具
 */
public class UserContextUtil {

    private static Map<String, UserContext> userContextMap = Collections.synchronizedMap(new HashMap<>());

    private static ThreadLocal<UserContext> userContextLocal = new ThreadLocal<>();

    /**
     * 获取当前上下文中的用户
     *
     * @return
     */
    public static User getUser() {
        if (Validator.isNotNull(userContextLocal.get())) {
            return userContextLocal.get().getUser();
        }
        return null;
    }

    public static UserContext getUserContext(String jwt) {
        if (Validator.isNull(jwt)) {
            return null;
        }
        UserContext userContext = userContextMap.get(jwt);
        return userContext;
    }

    public static void setUserContext(UserContext userContext) {
        userContextLocal.set(userContext);
    }

    public static void addUser(String jwt, User user) {
        UserContext userContext = new UserContext(user);
        userContextMap.put(jwt, userContext);
        userContextLocal.set(userContext);
    }

}
