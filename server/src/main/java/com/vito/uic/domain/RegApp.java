package com.vito.uic.domain;

import com.vito.common.model.enums.YesNoEnum;
import com.vito.storage.domain.BaseBusinessEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 23:57
 * 描述:
 */
@Entity
@Table(name = "sys_reg_app")
public class RegApp extends BaseBusinessEntity {

    private String appKey;
    private String appSecret;
    private String domain;

    /**
     * IP白名单
     */
    private String whiteIps;

    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum enable;

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public YesNoEnum getEnable() {
        return enable;
    }

    public void setEnable(YesNoEnum enable) {
        this.enable = enable;
    }
}
