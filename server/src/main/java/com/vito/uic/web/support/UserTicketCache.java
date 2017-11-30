package com.vito.uic.web.support;

import com.vito.common.model.Pair;
import com.vito.common.util.string.CodeGenerateUtil;
import com.vito.common.util.validate.Validator;
import com.vito.uic.domain.RegApp;
import com.vito.uic.domain.User;

import java.util.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:10
 * 描述:
 */
public class UserTicketCache {

    private static final Map<String, User> stUserCache = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, Pair<User, Date>> tgcUserCache = Collections.synchronizedMap(new HashMap<>());

    private static final List<RegApp> appsCache = new ArrayList<>();

    /**
     * 生成st
     *
     * @param user
     * @return
     */
    public static String genServiceTicket(User user) {
        String serviceTicket = CodeGenerateUtil.generateUUID();
        stUserCache.put(serviceTicket, user);
        return serviceTicket;
    }

    /**
     * 获取st对应的用户
     *
     * @param serviceTicket
     * @return
     */
    public static User getStUser(String serviceTicket) {
        User user = stUserCache.get(serviceTicket);
        if (Validator.isNotNull(user)) {
            stUserCache.remove(serviceTicket);
        }
        return user;
    }

    /**
     * 生成tgc cookie
     *
     * @param user
     * @return
     */
    public static String genTicketGrantCookie(User user) {
        String ticketGrantCookie = CodeGenerateUtil.generateUUID();
        Pair<User, Date> userDatePair = new Pair<>(user, new Date());
        tgcUserCache.put(ticketGrantCookie, userDatePair);
        return ticketGrantCookie;
    }

    /**
     * 获取tgc对应的用户
     *
     * @param ticketGrantCookie
     * @return
     */
    public static User getTgcUser(String ticketGrantCookie) {
        Pair<User, Date> userDatePair = tgcUserCache.get(ticketGrantCookie);
        if (Validator.isNotNull(userDatePair)) {
            return userDatePair.getFirst();
        }
        return null;
    }



}
