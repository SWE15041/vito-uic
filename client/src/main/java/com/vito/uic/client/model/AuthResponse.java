package com.vito.uic.client.model;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:19
 * 描述:
 */
public class AuthResponse {

    private Boolean result;

    private String errMsg;

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

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(Set<String> roleCodes) {
        this.roleCodes = roleCodes;
    }
}
