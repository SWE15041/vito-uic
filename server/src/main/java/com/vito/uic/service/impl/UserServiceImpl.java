package com.vito.uic.service.impl;

import com.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.vito.common.util.validate.Validator;
import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.User;
import com.vito.uic.domain.UserMapper;
import com.vito.uic.domain.UserRepository;
import com.vito.uic.domain.UserRole;
import com.vito.uic.service.UserRoleService;
import com.vito.uic.service.UserService;
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
public class UserServiceImpl extends EntityCRUDServiceImpl<User, Long> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public User findByLoginName(String loginName) {
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
    public User get(Long id) {
        User user = super.get(id);
        List<Long> userRoles = userRoleService.findUserRoles(id);
        user.setRoleIds(new HashSet<>(userRoles));
        return user;
    }

    @Transactional
    @Override
    public User save(User user) {
        handleUserRoles(user);
        if (Validator.isNull(user.getPassword())) {
            user.setPassword(MD5EncryptUtil.encrypt(user.getMobile()));
        }
        //todo 判断用户登录名是否已存在
        return super.save(user);
    }

    private void handleUserRoles(User user) {
        if (Validator.isNotNull(user.getId()) && Validator.isNotNull(user.getRoleIds())) {
            userRoleService.deleteByUserId(user.getId());
            user.getRoleIds().forEach(roleId -> {
                userRoleService.save(new UserRole(user.getId(), roleId));
            });
        }
    }

    @Transactional
    @Override
    public User update(User user) {
        handleUserRoles(user);
        return super.update(user);
    }
}
