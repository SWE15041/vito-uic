/**
 * Created by lenovo on 2016/2/14.
 */
package com.vito.uic.domain;


import com.vito.common.model.enums.YesNoEnum;
import com.vito.storage.domain.BaseBusinessEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */
@Entity
@Table(name = "sys_role")
public class Role extends BaseBusinessEntity {

    private String code;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum isDefault;

    /**
     * 每个用户对应有多条站内消息
     */
//    private Set<SysResource> sysResources = null;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YesNoEnum getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(YesNoEnum isDefault) {
        this.isDefault = isDefault;
    }
}
