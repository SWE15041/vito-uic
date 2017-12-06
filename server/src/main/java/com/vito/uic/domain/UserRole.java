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
@Table(name = "sys_user_role")
public class UserRole extends BaseBusinessEntity {

    private Long userId;

    private Long roleId;

    public UserRole() {
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
