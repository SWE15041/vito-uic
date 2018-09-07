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
@Table(name = "sys_user_role")
public class SysUserRole extends BaseBusinessEntity<Long> {

    /**
     *用户id
     */
    private Long userId;

    /**
     *角色id
     */
    private Long roleId;

    public SysUserRole() {
    }

    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
