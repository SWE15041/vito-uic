package com.jay.vito.uic.service.impl;

import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.domain.SysUserMapper;
import com.jay.vito.uic.domain.SysUserRepository;
import com.jay.vito.uic.domain.SysUserRole;
import com.jay.vito.uic.service.SysUserRoleService;
import com.jay.vito.uic.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

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
public class SysUserServiceImpl extends BusinessEntityCRUDServiceImpl<SysUser, Long> implements SysUserService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser findByLoginName(String loginName) {
        return sysUserRepository.findByLoginName(loginName);
    }

    @Override
    public Set<String> findUserResources(Long userId) {
        Long groupId = UserContextHolder.getCurrentGroupId();
        return new HashSet<>(sysUserMapper.queryUserResources(userId, groupId));
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
            throw new HttpBadRequestException("此手机号已注册过账户");
        }
        boolean existsByLoginName = sysUserRepository.existsByLoginName(user.getLoginName());
        if (existsByLoginName) {
            throw new HttpBadRequestException("登录名已存在");
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
        Long currentGroupId = UserContextHolder.getCurrentGroupId();
        if (Validator.isNotNull(user.getId())) {
            sysUserRoleService.deleteByUserIdAndGroupId(user.getId(), currentGroupId);
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

    @Transactional
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

    public boolean isManager(Long userId) {
        SysUser sysUser = get(userId);
        if (sysUser == null) {
            throw new RuntimeException("此用户不存在");
        }
        YesNoEnum manager = sysUser.getManager();
        if (manager == YesNoEnum.YES) {
            return true;
        }
        return false;
    }

    /**
     * 如果用户编号为userId的用户包含codeType类型的角色编码；则返回true;
     *
     * @param userId
     * @param codeType
     * @return
     */
    @Override
    public boolean isRoleCode(Long userId, String codeType) {
        List<String> roleCodes = sysUserMapper.queryUserRoles(userId);
        if (roleCodes.contains(codeType)) {
            return true;
        }
//        for (String roleCode : roleCodes) {
//            if(roleCode.equals(codeType)){
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public SysUser existsOpenId(String openId) {
        boolean existsOpenId = sysUserRepository.existsByWechatOpenId(openId);
        if (existsOpenId) {
            SysUser sysUser = sysUserRepository.findByWechatOpenId(openId);
            return sysUser;
        }
        return null;
    }

    @Transactional
    @Override
    public SysUser bind(String mobile, String openId) {
        //判断用户的openId是否绑定手机号
        boolean exists = sysUserRepository.existsByWechatOpenIdAndMobile(openId, mobile);
        if (exists) {
            throw new RuntimeException("用户已绑定");
        }
        SysUser user = sysUserRepository.findByMobile(mobile);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setWechatOpenId(openId);
        update(user);
        return user;
    }

    public static void main(String[] args) {
        System.out.println(MD5EncryptUtil.encrypt("abc"));
    }
}
