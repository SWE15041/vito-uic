package com.vito.uic.service.impl;

import com.vito.common.util.validate.Validator;
import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.Role;
import com.vito.uic.domain.RoleRepository;
import com.vito.uic.domain.RoleResource;
import com.vito.uic.service.RoleResourceService;
import com.vito.uic.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class RoleServiceImpl extends EntityCRUDServiceImpl<Role, Long> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    @Transactional
    @Override
    public Role save(Role role) {
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
}
