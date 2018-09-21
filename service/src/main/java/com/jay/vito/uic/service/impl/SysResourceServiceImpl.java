package com.jay.vito.uic.service.impl;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.constant.ResourceType;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.domain.SysResourceRepository;
import com.jay.vito.uic.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class SysResourceServiceImpl extends EntityCRUDServiceImpl<SysResource, Long> implements SysResourceService {

    @Autowired
    private SysResourceRepository sysResourceRepository;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleResourceService sysRoleResourceService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    protected JpaRepository<SysResource, Long> getRepository() {
        return sysResourceRepository;
    }

    @Override
    public List<SysResource> findEnableResources() {
        List<SysResource> resources = sysResourceRepository.findByEnable(YesNoEnum.YES);
        return resources;
    }

    @Override
    public List<SysResource> findByResourceType(ResourceType resourceType) {
        List<SysResource> resources = sysResourceRepository.findByEnableAndResourceType(YesNoEnum.YES, resourceType);
        return resources;
    }

    @Override
    public List<SysResource> getUserResources(Long currentUserId) {
        Set<String> resourceCodes = sysUserService.findUserResources(currentUserId);
        List<SysResource> sysResources = findByResourceType(ResourceType.MENU);
        List<SysResource> menuResources = new ArrayList<>();

        for (SysResource resource : sysResources) {
            if (resourceCodes.contains(resource.getCode())) {
                menuResources.add(resource);
            }
        }
        return menuResources;
    }
}
