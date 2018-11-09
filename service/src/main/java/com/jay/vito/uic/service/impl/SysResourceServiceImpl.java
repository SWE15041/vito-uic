package com.jay.vito.uic.service.impl;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.uic.constant.ResourceType;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.domain.SysResourceRepository;
import com.jay.vito.uic.service.SysResourceService;
import com.jay.vito.uic.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
public class SysResourceServiceImpl extends BusinessEntityCRUDServiceImpl<SysResource, Long> implements SysResourceService {

    @Autowired
    private SysResourceRepository sysResourceRepository;
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
    public List<SysResource> getUserMenu(Long currentUserId) {
//        boolean manager = sysUserService.isManager(currentUserId);//如果该用户是管理员，则返回所有的菜单资源
//        if (manager) {
//            List<SysResource> manMenu = findByResourceType(ResourceType.MENU);
//            return manMenu;
//        }
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


    @Override
    public List<SysResource> getUserResources(Long currentUserId) {
//        boolean manager = sysUserService.isManager(currentUserId);//如果该用户是管理员，则返回所有的资源
//        if (manager) {
//            List<SysResource> manResources = sysResourceRepository.findByEnable(YesNoEnum.YES);
//            return manResources;
//        }
        Set<String> resourceCodes = sysUserService.findUserResources(currentUserId);//获取该用户所有的资源的code；
        List<SysResource> sysResources = sysResourceRepository.findByEnable(YesNoEnum.YES);
        List<SysResource> resources = new ArrayList<>();
        for (SysResource sysResource : sysResources) {
            if (resourceCodes.contains(sysResource.getCode())) {
                resources.add(sysResource);
            }
        }
        return resources;
    }

    @Override
    public List<Long> getResourceIds(Long usreId) {
//        boolean manager = sysUserService.isManager(usreId);//如果该用户是管理员，则返回所有的资源id
//        if (manager) {
//            List<SysResource> sysResources = findEnableResources();//已激活的所有资源
//            List<Long> resourceIds=new ArrayList<>();
//            for (SysResource sysResource : sysResources) {
//                resourceIds.add(sysResource.getId());
//            }
//            return resourceIds;
//        }

        List<SysResource> sysResources = findEnableResources();//已激活的所有资源
        Set<String> resourceCodes = sysUserService.findUserResources(usreId);//用户所拥有的资源code
        List<Long> resourceIds = new ArrayList<>();
        for (SysResource sysResource : sysResources) {
            if (resourceCodes.contains(sysResource.getCode())) {
                resourceIds.add(sysResource.getId());
            }
        }
        return resourceIds;
    }
}
