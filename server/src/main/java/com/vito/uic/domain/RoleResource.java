package com.vito.uic.domain;

import com.vito.storage.domain.BaseBusinessEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 23:57
 * 描述: 角色资源
 */
@Entity
@Table(name = "sys_role_resource")
public class RoleResource extends BaseBusinessEntity {

    private Long roleId;

    private Long resourceId;

    public RoleResource() {
    }

    public RoleResource(Long roleId, Long resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
