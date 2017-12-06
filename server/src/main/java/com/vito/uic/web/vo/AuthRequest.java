package com.vito.uic.web.vo;

/**
 * 作者: zhaixm
 * 日期: 2017/11/26 0:00
 * 描述:
 */
public class AuthRequest {

    private String appKey;
    private String appSecret;
    private String appToken;

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

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

}
