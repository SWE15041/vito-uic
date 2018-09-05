package com.jay.vito.uic.service.impl;

import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.domain.SysUserMapper;
import com.jay.vito.uic.domain.SysUserRepository;
import com.jay.vito.uic.domain.SysUserRole;
import com.jay.vito.uic.service.SysUserRoleService;
import com.jay.vito.uic.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class SysUserServiceImpl extends EntityCRUDServiceImpl<SysUser, Long> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysUserRepository sysUserRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    protected JpaRepository<SysUser, Long> getRepository() {
        return sysUserRepository;
    }

    @Override
    public SysUser findByLoginName(String loginName) {
        return sysUserRepository.findByLoginName(loginName);
    }

    @Override
    public Set<String> findUserResources(Long userId) {
        return new HashSet<>(sysUserMapper.queryUserResources(userId));
    }

    public List<Map<String, Object>> query(Map<String, Object> params) {
        return sysUserMapper.selectList(params);
    }

    @Override
    public SysUser get(Long id) {
        SysUser user = super.get(id);
        List<Long> userRoles = sysUserRoleService.findUserRoles(id);
        user.setRoleIds(new HashSet<>(userRoles));
        return user;
    }

    @Transactional
    @Override
    public SysUser save(SysUser user) {
        handleUserRoles(user);
        if (Validator.isNull(user.getPassword())) {
            user.setPassword(MD5EncryptUtil.encrypt(user.getMobile()));
        } else {
            user.setPassword(MD5EncryptUtil.encrypt(user.getPassword()));
        }
        //todo 判断用户登录名是否已存在
        return super.save(user);
    }

    private void handleUserRoles(SysUser user) {
        if (Validator.isNotNull(user.getId())) {
            sysUserRoleService.deleteByUserId(user.getId());
            if (Validator.isNotNull(user.getRoleIds())) {
                user.getRoleIds().forEach(roleId -> {
                    sysUserRoleService.save(new SysUserRole(user.getId(), roleId));
                });
            }
        }
    }

    @Transactional
    @Override
    public SysUser update(SysUser user) {
        handleUserRoles(user);
        return super.update(user);
    }

    public static void main(String[] args) {
        System.out.println(MD5EncryptUtil.encrypt("abc"));
    }
}
