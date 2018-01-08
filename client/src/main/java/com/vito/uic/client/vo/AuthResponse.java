package com.vito.uic.client.vo;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:19
 * 描述:
 */
public class AuthResponse {

    private String token;
    private Long userId;
    private String userName;
    private Set<String> resources;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }
}
