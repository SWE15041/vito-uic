package com.vito.uic.web.vo;

/**
 * 作者: zhaixm
 * 日期: 2017/11/26 0:00
 * 描述:
 */
public class AuthRequest {

    private String appKey;
    private String appSecret;
    private String serviceTicket;
    private String tickGrantCookie;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getServiceTicket() {
        return serviceTicket;
    }

    public void setServiceTicket(String serviceTicket) {
        this.serviceTicket = serviceTicket;
    }

    public String getTickGrantCookie() {
        return tickGrantCookie;
    }

    public void setTickGrantCookie(String tickGrantCookie) {
        this.tickGrantCookie = tickGrantCookie;
    }
}
