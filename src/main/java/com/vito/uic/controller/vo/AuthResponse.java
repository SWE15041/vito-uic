package com.vito.uic.controller.vo;

import com.vito.uic.domain.User;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:19
 * 描述:
 */
public class AuthResponse {

    private Boolean result;

    private User user;

    private Set<String> roleCodes;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(Set<String> roleCodes) {
        this.roleCodes = roleCodes;
    }
}
