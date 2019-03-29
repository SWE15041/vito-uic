package com.jay.vito.uic.server.web.controller;

import com.google.common.collect.ImmutableMap;
import com.jay.vito.common.exception.*;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.common.util.string.CodeGenerateUtil;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.TokenUtil;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.jay.vito.uic.client.vo.AuthResponse;
import com.jay.vito.uic.server.domain.SysUser;
import com.jay.vito.uic.server.service.SysUserService;
import com.jay.vito.uic.server.web.vo.SysUserVo;
import com.jay.vito.website.core.cache.SystemDataHolder;
import com.jay.vito.website.core.cache.SystemParamKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:18
 * 描述: 认证控制器
 */
@RestController
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	private Map<String, Long> userCache = new HashMap<>();

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 登录验证
	 *
	 * @param user
	 */
	@IgnoreUserAuth
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public AuthResponse login(@RequestBody SysUser user) {
		SysUser loginUser = sysUserService.getByLoginName(user.getLoginName());
		if (Validator.isNull(loginUser)) {
			throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
		}
		if (loginUser.getEnable() == YesNoEnum.NO) {
			throw new BusinessException("该用户已被禁用");
		}
		if (loginUser.getLoginable() == YesNoEnum.NO) {
			throw new BusinessException("该用户没有登录权限");
		}
		if (MD5EncryptUtil.encrypt(user.getPassword()).equals(loginUser.getPassword())) {
			AuthResponse authResp = genTokenResponse(loginUser);
			// 获取用户分配的应用及相关资源
			Set<String> resources = sysUserService.findUserResources(loginUser.getId());
			authResp.setResources(resources);
			return authResp;
		} else {
			throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
		}
	}

	/**
	 * 生成token返回数据
	 *
	 * @param loginUser
	 * @return
	 */
	private AuthResponse genTokenResponse(SysUser loginUser) {
		TokenData tokenData = new TokenData(loginUser.getId(), loginUser.getGroupId());
		tokenData.setManager(loginUser.manager());
		tokenData.setUicDomain(SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class));
		String token = TokenUtil.genToken(tokenData);
		AuthResponse authResp = new AuthResponse();
		authResp.setToken("Bearer " + token);
		authResp.setUserId(loginUser.getId());
		authResp.setUserName(loginUser.getName());
		authResp.setManager(loginUser.manager());
		return authResp;
	}

	/**
	 * 刷新token
	 *
	 * @return
	 */
	@GetMapping("/token/refresh")
	public AuthResponse refreshToken() {
		SysUser sysUser = sysUserService.get(UserContextHolder.getCurrentUserId());
		AuthResponse authResponse = genTokenResponse(sysUser);
		return authResponse;
	}

	/**
	 * 获取一次性访问token  用于对接第三方系统登录
	 *
	 * @return
	 */
	@RequestMapping(value = "/onceToken", method = RequestMethod.GET)
	public Map<String, Object> getToken() {
		String token = CodeGenerateUtil.generateUUID();
		userCache.put(token, UserContextHolder.getCurrentUserId());
		return ImmutableMap.of("token", token);
	}

	/**
	 * 一次性token获取用户信息
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public Map<String, Object> info(@RequestParam String token) {
		Long userId = userCache.get(token);
		if (Validator.isNull(userId)) {
			throw new HttpUnauthorizedException("token无效", "NOT_VALID_TOKEN");
		}
		SysUser user = sysUserService.get(userId);
		Map<String, Object> data = new HashMap<>();
		data.put("datas", user);
		data.put("msg", "登录成功");
		return data;
	}

	/**
	 * 忘记密码
	 *
	 * @param sysUserVo
	 * @param session
	 * @return
	 */
	@IgnoreUserAuth
	@RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
	public boolean resetPwd(@RequestBody SysUserVo sysUserVo, HttpSession session) {
		// 验证手机号验证码 messageValidCode
		String validMessage = String.valueOf(session.getAttribute("MessageCode"));
		String messageValidCode = sysUserVo.getMessageValidCode();
		if (!validMessage.equals(messageValidCode)) {
			throw new HttpBadRequestException("验证码错误", "INVALID_MESSAGE_VALIDCODE");
		}
		SysUser sysUser = sysUserService.getByMobile(sysUserVo.getMobile());
		boolean result = sysUserService.updatePwd(sysUser.getId(), sysUserVo.getPassword());
		return result;
	}

	public static void main(String[] args) {
		System.out.println(MD5EncryptUtil.encrypt("admin@qzsdk2018"));
	}

}
