package com.jay.vito.uic.server.service;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.server.domain.SysRole;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 角色服务
 */
public interface SysRoleService extends EntityCRUDService<SysRole, Long> {

    Long getRoleIdByCode(String code, Long groupId);

    List<SysRole> findByGroupId(Long groupId, YesNoEnum isDefault);

    List<SysRole> findAll(Long userId,Long groupId);

    List<Long> getResources(Long roleId);
}
