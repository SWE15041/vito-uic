package com.vito.uic.service.impl;

import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.RoleResource;
import com.vito.uic.domain.RoleResourceRepository;
import com.vito.uic.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class RoleResourceServiceImpl extends EntityCRUDServiceImpl<RoleResource, Long> implements RoleResourceService {

    @Autowired
    private RoleResourceRepository permissionRepository;

    @Override
    protected JpaRepository<RoleResource, Long> getRepository() {
        return permissionRepository;
    }

}
