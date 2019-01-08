package com.jay.vito.uic.server.service.impl;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.uic.client.service.BusinessEntityCRUDServiceImpl;
import com.jay.vito.uic.server.constant.ResourceType;
import com.jay.vito.uic.server.domain.SysResource;
import com.jay.vito.uic.server.repository.SysResourceRepository;
import com.jay.vito.uic.server.service.SysResourceService;
import com.jay.vito.uic.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhaixm
 */
public class SysResourceServiceImpl extends BusinessEntityCRUDServiceImpl<SysResource, Long> implements SysResourceService {

	@Autowired
	private SysResourceRepository sysResourceRepository;

	@Autowired
	private SysUserService sysUserService;

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
	public List<SysResource> getUserMenus(Long currentUserId) {
		// 如果该用户是管理员，则返回所有的菜单资源
		boolean manager = sysUserService.isManager(currentUserId);
		if (manager) {
			List<SysResource> menus = findByResourceType(ResourceType.MENU);
			return menus;
		}
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
		// 如果该用户是管理员，则返回所有的资源
		boolean manager = sysUserService.isManager(currentUserId);
		if (manager) {
			List<SysResource> resources = sysResourceRepository.findByEnable(YesNoEnum.YES);
			return resources;
		}
		// 获取该用户所有的资源的code；
		Set<String> resourceCodes = sysUserService.findUserResources(currentUserId);
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
		// 如果该用户是管理员，则返回所有的资源id
		boolean manager = sysUserService.isManager(usreId);
		if (manager) {
			// 已激活的所有资源
			List<SysResource> sysResources = findEnableResources();
			List<Long> resourceIds = new ArrayList<>();
			for (SysResource sysResource : sysResources) {
				resourceIds.add(sysResource.getId());
			}
			return resourceIds;
		}

		// 已激活的所有资源
		List<SysResource> sysResources = findEnableResources();
		// 用户所拥有的资源code
		Set<String> resourceCodes = sysUserService.findUserResources(usreId);
		List<Long> resourceIds = new ArrayList<>();
		for (SysResource sysResource : sysResources) {
			if (resourceCodes.contains(sysResource.getCode())) {
				resourceIds.add(sysResource.getId());
			}
		}
		return resourceIds;
	}
}
