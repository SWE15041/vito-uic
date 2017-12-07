package com.vito.uic.client.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/12/5 23:34
 * 描述:
 */
public class TokenData {

    private Long userId;
    private Long groupId;
    private String userName;
    private Boolean manager = false;
    private String uicDomain;
    private Set<String> appDomains = new HashSet<>();
    private Date loginTime;

    public TokenData() {
        this.loginTime = new Date();
    }

    public TokenData(Long userId) {
        this();
        this.userId = userId;
    }

    public TokenData(Long userId, Long groupId) {
        this(userId);
        this.groupId = groupId;
    }

    public TokenData(Long userId, Long groupId, Boolean manager) {
        this(userId, groupId);
        this.manager = manager;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean isManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public String getUicDomain() {
        return uicDomain;
    }

    public void setUicDomain(String uicDomain) {
        this.uicDomain = uicDomain;
    }

    public Set<String> getAppDomains() {
        return appDomains;
    }

    public void setAppDomains(Set<String> appDomains) {
        this.appDomains = appDomains;
    }

    public void addAppDomain(String appDomain) {
        this.appDomains.add(appDomain);
    }

    public boolean containApp(String appDomain) {
        return this.appDomains.contains(appDomain);
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
