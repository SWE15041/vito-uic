package com.jay.vito.uic.server.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.server.constant.ResourceType;
import com.jay.vito.uic.server.domain.SysResource;

import java.util.List;

/**
 * 描述: 资源服务
 * 日期: 2017/11/23 16:26
 *
 * @author zhaixm
 */
public interface SysResourceService extends EntityCRUDService<SysResource, Long> {

	/**
	 * 获取所有可用的资源
	 *
	 * @return
	 */
	List<SysResource> findEnableResources();

	/**
	 * 根据资源类型查询
	 *
	 * @param resourceType
	 * @return
	 */
	List<SysResource> findByResourceType(ResourceType resourceType);

	/**
	 * 获取用户资源
	 *
	 * @param currentUserId
	 * @return
	 */
	List<SysResource> getUserResources(Long currentUserId, ResourceType... resourceTypes);

	/**
	 * 获取用户资源id集合
	 *
	 * @param userId
	 * @return
	 */
	List<Long> getResourceIds(Long userId);
}
