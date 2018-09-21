package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysRoleResource;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 角色资源服务
 */
public interface SysRoleResourceService extends EntityCRUDService<SysRoleResource, Long> {

    List<Long> findRoleResources(Long roleId);

    void deleteByRoleId(Long roleId);

    List<SysRoleResource> findByRoleId(Long roleId);
}
