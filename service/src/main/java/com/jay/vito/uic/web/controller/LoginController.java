package com.jay.vito.uic.web.controller;

import com.google.common.collect.ImmutableMap;
import com.jay.vito.common.util.string.CodeGenerateUtil;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.TokenUtil;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.client.vo.AuthResponse;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.service.SysUserService;
import com.jay.vito.website.core.cache.SystemDataHolder;
import com.jay.vito.website.core.cache.SystemParamKeys;
import com.jay.vito.website.core.exception.ErrorCodes;
import com.jay.vito.website.core.exception.HttpException;
import com.jay.vito.website.core.exception.HttpUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public AuthResponse login(@RequestBody SysUser user) {
        SysUser loginUser = sysUserService.findByLoginName(user.getLoginName());
        if (Validator.isNull(loginUser)) {
            throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
        } else {
            if (MD5EncryptUtil.encrypt(user.getPassword()).equals(loginUser.getPassword())) {
                //todo 获取用户分配的应用及相关资源
                TokenData tokenData = new TokenData(loginUser.getId(), loginUser.getGroupId());
                tokenData.setManager(loginUser.manager());
                tokenData.setUicDomain(SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class));
//                tokenData.setAppDomains("");
                String token = TokenUtil.genToken(tokenData);
                AuthResponse authResp = new AuthResponse();
                authResp.setToken(token);
                authResp.setUserId(loginUser.getId());
                authResp.setUserName(loginUser.getName());
                authResp.setManager(loginUser.manager());
                Set<String> resources = sysUserService.findUserResources(loginUser.getId());
                authResp.setResources(resources);
                return authResp;
            } else {
                throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
            }
        }
    }

    @RequestMapping(value = "/api/wechatAuth", method = RequestMethod.POST)
    public void wechatAuth(@RequestParam String authCode) {

    }

    @RequestMapping(value = "/api/onceToken", method = RequestMethod.GET)
    public Map<String, Object> getToken() {
        String token = CodeGenerateUtil.generateUUID();
        userCache.put(token, UserContextHolder.getCurrentUserId());
        return ImmutableMap.of("token", token);
    }

    @RequestMapping(value = "/api/userInfo", method = RequestMethod.GET)
    public Map<String, Object> info(@RequestParam String token) {
        Long userId = userCache.get(token);
        if (Validator.isNull(userId)) {
            throw new HttpUnauthorizedException("token无效", "NOT_VALID_TOKEN");
        }
//        userCache.remove(token);
        SysUser user = sysUserService.get(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("datas", user);
        data.put("msg", "登录成功");
        return data;
    }

    public static void main(String[] args) {
        System.out.println(MD5EncryptUtil.encrypt("admin@qzsdk2018"));
    }

}
