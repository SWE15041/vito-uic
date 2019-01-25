package com.jay.vito.uic.client.service;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.client.domain.BaseBusinessEntity;

import java.io.Serializable;

public abstract class BusinessEntityCRUDServiceImpl<T extends BaseBusinessEntity<ID>, ID extends Serializable> extends EntityCRUDServiceImpl<T, ID> {

    @Override
    public T save(T entity) {
        if (entity.getCreateUser() == null) {
            entity.setCreateUser(UserContextHolder.getCurrentUserId());
        }

        if (entity.getGroupId() == null) {
            entity.setGroupId(UserContextHolder.getCurrentGroupId());
        }
        return super.save(entity);
    }
}
