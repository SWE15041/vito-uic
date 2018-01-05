package com.vito.storage.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 作者: zhaixm
 * 日期: 2016/4/12 22:58
 * 描述: 业务实体基础对象
 */
@MappedSuperclass
//@Access(AccessType.PROPERTY)
public class BaseBusinessEntity<ID extends Serializable> extends BaseEntity<ID> {

    private Long createUser;

    private Long groupId;

    @Column(name = "create_user")
    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}