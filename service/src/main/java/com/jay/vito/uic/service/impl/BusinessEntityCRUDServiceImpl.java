package com.jay.vito.uic.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.domain.BaseBusinessEntity;

import java.io.Serializable;

public abstract class BusinessEntityCRUDServiceImpl<T extends BaseBusinessEntity<ID>, ID extends Serializable> extends EntityCRUDServiceImpl<T, ID> {

    @Override
    public T save(T entity) {
        entity.setCreateUser(UserContextHolder.getCurrentUserId());
        entity.setGroupId(UserContextHolder.getCurrentGroupId());
        return super.save(entity);
    }
}
