package com.jay.vito.uic.service.impl;

import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.domain.UserMapper;
import com.jay.vito.uic.domain.UserRepository;
import com.jay.vito.uic.domain.UserRole;
import com.jay.vito.uic.service.UserRoleService;
import com.jay.vito.uic.service.UserService;
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
public class UserServiceImpl extends EntityCRUDServiceImpl<SysUser, Long> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserMapper userMapper;

    @Override
    protected JpaRepository<SysUser, Long> getRepository() {
        return userRepository;
    }

    @Override
    public SysUser findByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    @Override
    public Set<String> findUserResources(Long userId) {
        return new HashSet<>(userMapper.queryUserResources(userId));
    }

    public List<Map<String, Object>> query(Map<String, Object> params) {
        return userMapper.selectList(params);
    }

    @Override
    public SysUser get(Long id) {
        SysUser user = super.get(id);
        List<Long> userRoles = userRoleService.findUserRoles(id);
        user.setRoleIds(new HashSet<>(userRoles));
        return user;
    }

    @Transactional
    @Override
    public SysUser save(SysUser user) {
        handleUserRoles(user);
        if (Validator.isNull(user.getPassword())) {
            user.setPassword(MD5EncryptUtil.encrypt(user.getMobile()));
        }
        //todo 判断用户登录名是否已存在
        return super.save(user);
    }

    private void handleUserRoles(SysUser user) {
        if (Validator.isNotNull(user.getId())) {
            userRoleService.deleteByUserId(user.getId());
            if (Validator.isNotNull(user.getRoleIds())) {
                user.getRoleIds().forEach(roleId -> {
                    userRoleService.save(new UserRole(user.getId(), roleId));
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
