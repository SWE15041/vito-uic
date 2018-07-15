package com.jay.vito.storage.service;

import com.vito.common.util.bean.BeanUtil;
import com.vito.storage.domain.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/24 16:34
 * 描述:
 */
public abstract class EntityCRUDServiceImpl<T extends BaseEntity<ID>, ID extends Serializable> implements EntityCRUDService<T, ID> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract JpaRepository<T, ID> getRepository();

    @Override
    public List<T> getAll() {
        return getRepository().findAll();
    }

    @Override
    public T get(ID id) {
        return getRepository().findOne(id);
    }

    @Transactional
    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    @Override
    public T update(T entity) {
        return getRepository().save(entity);
    }

    @Transactional
    @Override
    public T updateNotNull(T entity) {
        T dbEntity = get(entity.getId());
        BeanUtil.copyNotNullProperties(dbEntity, entity);
        return update(dbEntity);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        getRepository().delete(entity);
    }

    @Transactional
    @Override
    public void delete(ID entityId) {
        getRepository().delete(entityId);
    }
}