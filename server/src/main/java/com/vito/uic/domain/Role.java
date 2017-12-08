/**
 * Created by lenovo on 2016/2/14.
 */
package com.vito.uic.domain;


import com.vito.common.model.enums.YesNoEnum;
import com.vito.storage.domain.BaseBusinessEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Long> resourceIds = new HashSet<>();

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

    @Transient
    public Set<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }

}
