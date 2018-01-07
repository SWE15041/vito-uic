package com.vito.storage.service;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/24 16:28
 * 描述:
 */
public interface EntityCRUDService<T, ID extends Serializable> {

    List<T> getAll();

    T get(ID id);

    T save(T entity);

    T update(T entity);

    T updateNotNull(T entity);

    void delete(T entity);

    void delete(ID entityId);

}
