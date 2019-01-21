package com.jay.vito.uic.server.service.impl;

import com.jay.vito.common.exception.BusinessException;
import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.client.service.BusinessEntityCRUDServiceImpl;
import com.jay.vito.uic.server.domain.SysUser;
import com.jay.vito.uic.server.domain.SysUserMapper;
import com.jay.vito.uic.server.domain.SysUserRole;
import com.jay.vito.uic.server.repository.SysUserRepository;
import com.jay.vito.uic.server.service.SysUserRoleService;
import com.jay.vito.uic.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;

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
	public SysUser getByLoginName(String loginName) {
		return sysUserRepository.findFirstByLoginNameAndGroupId(loginName, UserContextHolder.getCurrentGroupId());
	}

	@Override
	public SysUser getByMobile(String mobile) {
		return sysUserRepository.findFirstByMobileAndGroupId(mobile, UserContextHolder.getCurrentGroupId());
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
		if (user == null) {
			throw new BusinessException("用户不存在");
		}
		List<Long> userRoles = sysUserRoleService.findUserRoles(id);
		user.setRoleIds(new HashSet<>(userRoles));
		return user;
	}

	@Override
	public SysUser save(SysUser user) {
		String mobile = user.getMobile();
		boolean existsByMobile = sysUserRepository.existsByMobileAndGroupId(mobile, UserContextHolder.getCurrentGroupId());
		if (existsByMobile) {
			throw new HttpBadRequestException("此手机号已注册过账户");
		}
		boolean existsByLoginName = sysUserRepository.existsByLoginNameAndGroupId(user.getLoginName(), UserContextHolder.getCurrentGroupId());
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

	@Override
	public SysUser update(SysUser user) {
		handleUserRoles(user);
		return super.update(user);
	}

	@Override
	public boolean updatePwd(Long id, String pwd) {
		SysUser sysUser = super.get(id);
		if (sysUser == null) {
			throw new BusinessException("该用户不存在");
		}
		sysUser.setPassword(MD5EncryptUtil.encrypt(pwd));
		super.update(sysUser);
		return true;
	}


	@Override
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
	 * @param roleCode
	 * @return
	 */
	@Override
	public boolean containRoleCode(Long userId, String roleCode) {
		List<String> roleCodes = sysUserMapper.queryUserRoles(userId);
		if (roleCodes.contains(roleCode)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(MD5EncryptUtil.encrypt("abc"));
	}
}
