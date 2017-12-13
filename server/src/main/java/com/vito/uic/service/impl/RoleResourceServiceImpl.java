package com.vito.uic.service.impl;

import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.RoleResource;
import com.vito.uic.domain.RoleResourceRepository;
import com.vito.uic.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class RoleResourceServiceImpl extends EntityCRUDServiceImpl<RoleResource, Long> implements RoleResourceService {

    @Autowired
    private RoleResourceRepository roleResourceRepository;

    @Override
    protected JpaRepository<RoleResource, Long> getRepository() {
        return roleResourceRepository;
    }

    @Override
    public List<Long> findRoleResources(Long roleId) {
        List<RoleResource> roleResources = roleResourceRepository.findByRoleId(roleId);
        List<Long> resourceIds = new ArrayList<>();
        roleResources.forEach(roleResource -> {
            resourceIds.add(roleResource.getResourceId());
        });
        return resourceIds;
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        roleResourceRepository.deleteByRoleId(roleId);
    }
}
