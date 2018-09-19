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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

@ConditionalOnProperty(name = "uic.userService.enabled", matchIfMissing = true)
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
        String mobile = user.getMobile();
        boolean existsByMobile = sysUserRepository.existsByMobile(mobile);
        if (existsByMobile) {
            throw new RuntimeException("此手机号已注册过账户");
        }
        boolean existsByLoginName = sysUserRepository.existsByLoginName(user.getLoginName());
        if (existsByLoginName) {
            throw new RuntimeException("已有账户，登录");
        }
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

    public boolean updatePwd(SysUser user) {
        SysUser sysUser = sysUserRepository.findByMobile(user.getMobile());
        if (sysUser == null) {
            throw new RuntimeException("该手机号未注册使用过");
        }
        sysUser.setPassword(MD5EncryptUtil.encrypt(user.getPassword()));
        super.update(sysUser);
        return true;
    }

    @Override
    public Long getIdByLoginName(String loginName) {
        SysUser sysUser = sysUserRepository.findByLoginName(loginName);
        Long userId = sysUser.getId();
        return userId;
    }

    public static void main(String[] args) {
        System.out.println(MD5EncryptUtil.encrypt("abc"));
    }
}
