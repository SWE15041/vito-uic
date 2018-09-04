package com.jay.vito.uic.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.domain.RoleRepository;
import com.jay.vito.uic.domain.RoleResource;
import com.jay.vito.uic.domain.SysRole;
import com.jay.vito.uic.service.RoleResourceService;
import com.jay.vito.uic.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class RoleServiceImpl extends EntityCRUDServiceImpl<SysRole, Long> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected JpaRepository<SysRole, Long> getRepository() {
        return roleRepository;
    }

    @Transactional
    @Override
    public SysRole save(SysRole role) {
        Set<Long> resourceIds = role.getResourceIds();
        role = super.save(role);
        if (Validator.isNotNull(role.getId()) && Validator.isNotNull(resourceIds)) {
            roleResourceService.deleteByRoleId(role.getId());
            for (Long resourceId : resourceIds) {
                roleResourceService.save(new RoleResource(role.getId(), resourceId));
            }
        }
        return role;
    }

    @Override
    public Long getRoleIdByCode(String code) {
        SysRole sysRole = roleRepository.findFirstByCode(code);
        if(sysRole==null){
            throw new RuntimeException("角色类型不存在");
        }

        return sysRole.getId();
    }
}