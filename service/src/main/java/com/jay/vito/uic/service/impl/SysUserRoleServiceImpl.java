package com.jay.vito.uic.service.impl;

import com.jay.vito.uic.domain.SysUserRole;
import com.jay.vito.uic.domain.SysUserRoleRepository;
import com.jay.vito.uic.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class SysUserRoleServiceImpl extends BusinessEntityCRUDServiceImpl<SysUserRole, Long> implements SysUserRoleService {

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    public List<Long> findUserRoles(Long userId) {
        List<SysUserRole> sysUserRoles = sysUserRoleRepository.findByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        sysUserRoles.forEach(sysUserRole -> {
            roleIds.add(sysUserRole.getRoleId());
        });
        return roleIds;
    }

    @Transactional
    @Override
    public void deleteByUserIdAndGroupId(Long userId,Long groupId) {
        sysUserRoleRepository.deleteByUserIdAndGroupId(userId,groupId);
    }

    @Override
    public List<Long> findUserRoles(Long userId, Long groupId) {

        List<SysUserRole> sysUserRoles = sysUserRoleRepository.findByUserIdAndGroupId(userId,groupId);
        List<Long> roleIds = new ArrayList<>();
        sysUserRoles.forEach(sysUserRole -> {
            roleIds.add(sysUserRole.getRoleId());
        });
        return roleIds;
    }
}
