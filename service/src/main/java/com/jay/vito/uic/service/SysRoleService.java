package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.domain.SysRole;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 角色服务
 */
public interface SysRoleService extends EntityCRUDService<SysRole, Long> {

    Long getRoleIdByCode(String code,Long groupId);

    List<SysRole> finds(Long groupId);

    List<SysRole> findAll(Long userId);

    List<Long> getResources(Long roleId);
}
