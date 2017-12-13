package com.vito.uic.service;

import com.vito.storage.service.EntityCRUDService;
import com.vito.uic.domain.RoleResource;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 角色资源服务
 */
public interface RoleResourceService extends EntityCRUDService<RoleResource, Long> {

    List<Long> findRoleResources(Long roleId);

    void deleteByRoleId(Long roleId);

}
