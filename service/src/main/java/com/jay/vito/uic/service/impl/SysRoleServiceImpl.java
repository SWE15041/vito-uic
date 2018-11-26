package com.jay.vito.uic.service.impl;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.domain.SysRole;
import com.jay.vito.uic.domain.SysRoleRepository;
import com.jay.vito.uic.domain.SysRoleResource;
import com.jay.vito.uic.service.SysRoleResourceService;
import com.jay.vito.uic.service.SysRoleService;
import com.jay.vito.uic.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
public class SysRoleServiceImpl extends BusinessEntityCRUDServiceImpl<SysRole, Long> implements SysRoleService {

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Transactional
    @Override
    public SysRole save(SysRole role) {
        Set<Long> resourceIds = role.getResourceIds();
        role = super.save(role);
        //先删除角色资源表里原先的关联；再新增关联
        if (Validator.isNotNull(role.getId()) && Validator.isNotNull(resourceIds)) {
            sysRoleResourceService.deleteByRoleId(role.getId());
            for (Long resourceId : resourceIds) {
                sysRoleResourceService.save(new SysRoleResource(role.getId(), resourceId));
            }
        }
        return role;
    }

    @Override
    public SysRole update(SysRole sysRole) {
        Set<Long> resourceIds = sysRole.getResourceIds();
        sysRole = super.update(sysRole);
        if (Validator.isNotNull(sysRole.getId()) && Validator.isNotNull(resourceIds)) {
            sysRoleResourceService.deleteByRoleId(sysRole.getId());
            for (Long resourceId : resourceIds) {
                sysRoleResourceService.save(new SysRoleResource(sysRole.getId(), resourceId));
            }
        }
        return sysRole;
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        sysRoleResourceService.deleteByRoleId(id);
    }


    @Override
    public Long getRoleIdByCode(String code, Long groupId) {
        SysRole sysRole = sysRoleRepository.findFirstByCodeAndGroupId(code, groupId);
        if (sysRole == null) {
            throw new RuntimeException("角色类型不存在");
        }
        return sysRole.getId();
    }

    /**
     * 通过groupId获取角色记录
     *
     * @param groupId
     * @return
     */
    @Override
    public List<SysRole> findByGroupId(Long groupId, YesNoEnum isDefault) {
        List<SysRole> sysRoleList = sysRoleRepository.findAllByGroupIdAndIsDefault(groupId, isDefault);
        return sysRoleList;
    }

    /*
     * 通过userId和groupId获取该用户的所在公司的所有角色记录数据
     * */
    @Override
    public List<SysRole> findAll(Long userId,Long groupId) {
        List<Long> roleIds = sysUserRoleService.findUserRoles(userId,groupId);

        List<SysRole> sysRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysRole sysRole = get(roleId);
            sysRoles.add(sysRole);
        }
        return sysRoles;
    }

    @Override
    public List<Long> getResources(Long roleId) {
        List<Long> resourceIds = sysRoleResourceService.findRoleResources(roleId);
        return resourceIds;
    }
}
