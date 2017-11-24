package com.vito.uic.service;

import com.vito.storage.service.EntityCRUDServiceImpl;
import com.vito.uic.domain.User;
import com.vito.uic.domain.UserMapper;
import com.vito.uic.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class UserServiceImpl extends EntityCRUDServiceImpl<User, Long> implements UserService {

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
    public List<Map<String, Object>> query(Map<String, Object> params) {
        return userMapper.selectList(params);
//        return null;
    }
}
