package com.jay.vito.uic.domain;

import com.jay.vito.storage.domain.BaseBusinessEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 23:57
 * 描述: 角色资源
 */
@Entity
@Table(name = "sys_role_resource")
public class SysRoleResource extends BaseBusinessEntity<Long> {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 资源id
     */
    private Long resourceId;

    public SysRoleResource() {
    }

    public SysRoleResource(Long roleId, Long resourceId) {
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
