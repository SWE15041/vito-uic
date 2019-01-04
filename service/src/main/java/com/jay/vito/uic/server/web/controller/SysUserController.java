package com.jay.vito.uic.server.web.controller;

import com.jay.vito.common.exception.ErrorCodes;
import com.jay.vito.common.exception.HttpException;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.server.domain.SysUser;
import com.jay.vito.uic.server.service.SysUserService;
import com.jay.vito.uic.server.web.vo.PwdModifyVo;
import com.jay.vito.website.web.controller.BaseGridCRUDController;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口控制器
 *
 * @author zhaixm
 * @date 2017/11/23 18:18
 */
@RestController
@RequestMapping("/users")
public class SysUserController extends BaseGridCRUDController<SysUser, Long, SysUserService> {

	/**
	 * 用户更新
	 *
	 * @param id
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Override
	public SysUser update(@PathVariable("id") Long id, @RequestBody SysUser user) {
		user.setPassword(null);
		return super.update(id, user);
	}

	/**
	 * 个人信息获取
	 *
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public SysUser info() {
		return super.get(UserContextHolder.getCurrentUserId());
	}

	/**
	 * 个人信息更新
	 *
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.PUT)
	public SysUser update(@RequestBody SysUser user) {
		if (!UserContextHolder.getCurrentUserId().equals(user.getId())) {
			throw HttpException.of(ErrorCodes.POWERLESS_MODIFY);
		}
		user.setPassword(null);
		return super.update(UserContextHolder.getCurrentUserId(), user);
	}

	/**
	 * 个人修改密码（个人自主修改）
	 *
	 * @param pwdModifyVo
	 * @return
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.PUT)
	public Boolean modifyPwd(@RequestBody PwdModifyVo pwdModifyVo) {
		String origPwd = pwdModifyVo.getOldPwd();
		SysUser user = entityService.get(UserContextHolder.getCurrentUserId());
		if (!user.getPassword().equals(MD5EncryptUtil.encrypt(origPwd))) {
			throw HttpException.of(ErrorCodes.INVALID_PASSWORD);
		}
		user.setPassword(MD5EncryptUtil.encrypt(pwdModifyVo.getNewPwd()));
		entityService.update(user);
		return true;
	}


}
