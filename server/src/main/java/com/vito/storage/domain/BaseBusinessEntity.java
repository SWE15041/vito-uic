package com.vito.storage.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 作者: zhaixm
 * 日期: 2016/4/12 22:58
 * 描述: 业务实体基础对象
 */
@MappedSuperclass
//@Access(AccessType.PROPERTY)
public class BaseBusinessEntity extends BaseEntity {

    private Long userId;

    private Long groupId;

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}