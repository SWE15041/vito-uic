package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.UserRole;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 用户角色服务
 */
public interface UserRoleService extends EntityCRUDService<UserRole, Long> {

    List<Long> findUserRoles(Long userId);

    void deleteByUserId(Long userId);

}
