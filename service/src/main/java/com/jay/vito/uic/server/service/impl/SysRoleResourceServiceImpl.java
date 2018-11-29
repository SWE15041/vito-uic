package com.jay.vito.uic.server.service.impl;

import com.jay.vito.uic.client.service.BusinessEntityCRUDServiceImpl;
import com.jay.vito.uic.server.domain.SysRoleResource;
import com.jay.vito.uic.server.repository.SysRoleResourceRepository;
import com.jay.vito.uic.server.service.SysRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SysRoleResourceServiceImpl extends BusinessEntityCRUDServiceImpl<SysRoleResource, Long> implements SysRoleResourceService {

    @Autowired
    private SysRoleResourceRepository sysRoleResourceRepository;

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
