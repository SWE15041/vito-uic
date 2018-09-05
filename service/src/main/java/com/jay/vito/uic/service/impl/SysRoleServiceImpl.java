package com.jay.vito.uic.service.impl;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.domain.SysRoleRepository;
import com.jay.vito.uic.domain.SysRoleResource;
import com.jay.vito.uic.domain.SysRole;
import com.jay.vito.uic.service.SysRoleResourceService;
import com.jay.vito.uic.service.SysRoleService;
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
public class SysRoleServiceImpl extends EntityCRUDServiceImpl<SysRole, Long> implements SysRoleService {

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    protected JpaRepository<SysRole, Long> getRepository() {
        return sysRoleRepository;
    }

    @Transactional
    @Override
    public SysRole save(SysRole role) {
        Set<Long> resourceIds = role.getResourceIds();
        role = super.save(role);
        if (Validator.isNotNull(role.getId()) && Validator.isNotNull(resourceIds)) {
            sysRoleResourceService.deleteByRoleId(role.getId());
            for (Long resourceId : resourceIds) {
                sysRoleResourceService.save(new SysRoleResource(role.getId(), resourceId));
            }
        }
        return role;
    }

    @Override
    public Long getRoleIdByCode(String code) {
        SysRole sysRole = sysRoleRepository.findFirstByCode(code);
        if(sysRole==null){
            throw new RuntimeException("角色类型不存在");
        }
        return sysRole.getId();
    }
}
