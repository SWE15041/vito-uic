package com.jay.vito.uic.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.domain.SysRoleResource;
import com.jay.vito.uic.domain.SysRoleResourceRepository;
import com.jay.vito.uic.service.SysRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class SysRoleResourceServiceImpl extends EntityCRUDServiceImpl<SysRoleResource, Long> implements SysRoleResourceService {

    @Autowired
    private SysRoleResourceRepository sysRoleResourceRepository;

    @Override
    protected JpaRepository<SysRoleResource, Long> getRepository() {
        return sysRoleResourceRepository;
    }

    @Override
    public List<Long> findRoleResources(Long roleId) {
        List<SysRoleResource> sysRoleResources = sysRoleResourceRepository.findByRoleId(roleId);
        List<Long> resourceIds = new ArrayList<>();
        sysRoleResources.forEach(sysRoleResource -> {
            resourceIds.add(sysRoleResource.getResourceId());
        });
        return resourceIds;
    }

    @Transactional
    @Override
    public void deleteByRoleId(Long roleId) {
        sysRoleResourceRepository.deleteByRoleId(roleId);
    }

    @Override
    public List<SysRoleResource> findByRoleId(Long roleId) {
        List<SysRoleResource> sysRoleResourceList = sysRoleResourceRepository.findByRoleId(roleId);
        return sysRoleResourceList;
    }
}
