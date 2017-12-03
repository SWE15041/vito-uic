package com.vito.uic.client.model;

import java.util.Date;

/**
 * 作者: zhaixm
 * 日期: 2017/12/3 23:30
 * 描述:
 */
public class UserContext {

    private User user;
    private Date loginTime;

    public UserContext(User user) {
        this.user = user;
        this.loginTime = new Date();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
