package com.jay.vito.uic.server.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.server.domain.SysUser;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 用户服务
 */
public interface SysUserService extends EntityCRUDService<SysUser, Long> {

	/**
	 * 根据登录名获取用户
	 *
	 * @param loginName
	 * @return
	 */
	SysUser getByLoginName(String loginName);

	/**
	 * 获取用户所有资源编码
	 *
	 * @param userId
	 * @return
	 */
	Set<String> findUserResources(Long userId);

	/**
	 * 更新密码
	 *
	 * @param id  用户id
	 * @param pwd 新密码
	 * @return
	 */
	boolean updatePwd(Long id, String pwd);

	/**
	 * 根据手机号查找用户
	 *
	 * @param mobile
	 * @return
	 */
	SysUser getByMobile(String mobile);

	/**
	 * 是否管理员
	 *
	 * @param userId
	 * @return
	 */
	boolean isManager(Long userId);

	/**
	 * 是否包含指定角色
	 *
	 * @param userId
	 * @param roleCode
	 * @return
	 */
	boolean containRoleCode(Long userId, String roleCode);

}
