package com.jay.vito.uic.server.web.vo;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Transient;

public class SysUserVo {


    /**
     * 短信验证码
     */
    @Transient
    private String messageValidCode;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 手机号
     */
    private String mobile;


    public String getMessageValidCode() {
        return messageValidCode;
    }

    public void setMessageValidCode(String messageValidCode) {
        this.messageValidCode = messageValidCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
