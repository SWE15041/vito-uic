package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysUser;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 用户服务
 */
public interface SysUserService extends EntityCRUDService<SysUser, Long> {

    SysUser findByLoginName(String loginName);

    Set<String> findUserResources(Long userId);

    boolean updatePwd(SysUser sysUser);

    Long getIdByLoginName(String loginName);

}
