package com.jay.vito.uic.server.service.impl;

import com.google.common.collect.Lists;
import com.jay.vito.common.exception.BusinessException;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.common.util.validate.Validator;
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

	/**
	 * 获取用户拥有的资源
	 *
	 * @param userId
	 * @param resourceTypes
	 * @return
	 */
	@Override
	public List<SysResource> getUserResources(Long userId, ResourceType... resourceTypes) {
		boolean manager = sysUserService.isManager(userId);
		List<SysResource> userResources = new ArrayList<>();
		List<SysResource> resources;
		if (Validator.isNull(resourceTypes)) {
			resources = sysResourceRepository.findByEnable(YesNoEnum.YES);
		} else {
			resources = sysResourceRepository.findByEnableAndResourceTypeIn(YesNoEnum.YES, Lists.newArrayList(resourceTypes));
		}
		// 如果该用户是管理员，则返回所有的资源
		if (manager) {
			userResources = resources;
		} else {
			// 获取该用户所有的资源的code；
			Set<String> resourceCodes = sysUserService.findUserResources(userId);
			for (SysResource resource : resources) {
				if (resourceCodes.contains(resource.getCode())) {
					userResources.add(resource);
				}
			}
		}
		return userResources;
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

	@Override
	public void delete(Long entityId) {
		validDeletable(entityId);
		super.delete(entityId);
	}

	private void validDeletable(Long entityId) {
		boolean existsChildren = sysResourceRepository.existsByParentId(entityId);
		if (existsChildren) {
			throw new BusinessException("该条记录有下级资源，不可删除");
		}
	}

	@Override
	public void delete(SysResource entity) {
		validDeletable(entity.getId());
		super.delete(entity);
	}
}
