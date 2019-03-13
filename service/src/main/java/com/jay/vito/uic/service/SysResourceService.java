package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.constant.ResourceType;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.web.vo.ResourceNode;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 资源服务
 */
public interface SysResourceService extends EntityCRUDService<SysResource, Long> {

    List<SysResource> findEnableResources();

    List<SysResource> findByResourceType(ResourceType resourceType);

    List<SysResource> getUserMenu(Long currentUserId);

    List<SysResource> getUserResources(Long currentUserId);

    List<Long> getResourceIds(Long userId);

    List<SysResource> getAllResources();

    /**
     * 给资源排序
     * @param resources
     * @return
     */
    List<ResourceNode> sortResources(List<SysResource> resources);

}
