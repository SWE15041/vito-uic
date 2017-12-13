package com.vito.uic.service.impl;

import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.UserRole;
import com.vito.uic.domain.UserRoleRepository;
import com.vito.uic.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class UserRoleServiceImpl extends EntityCRUDServiceImpl<UserRole, Long> implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    protected JpaRepository<UserRole, Long> getRepository() {
        return userRoleRepository;
    }

    @Override
    public List<Long> findUserRoles(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        userRoles.forEach(userRole -> {
            roleIds.add(userRole.getRoleId());
        });
        return roleIds;
    }

    @Override
    public void deleteByUserId(Long userId) {
        userRoleRepository.deleteByUserId(userId);
    }
}
