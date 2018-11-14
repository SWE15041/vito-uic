package com.jay.vito.uic.web.controller;

import com.google.common.collect.ImmutableMap;
import com.jay.vito.common.exception.ErrorCodes;
import com.jay.vito.common.exception.HttpBadRequestException;
import com.jay.vito.common.exception.HttpException;
import com.jay.vito.common.exception.HttpUnauthorizedException;
import com.jay.vito.common.util.bean.BeanUtil;
import com.jay.vito.common.util.string.CodeGenerateUtil;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.TokenData;
import com.jay.vito.uic.client.core.TokenUtil;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.jay.vito.uic.client.vo.AuthResponse;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.service.SysUserService;
import com.jay.vito.uic.web.vo.SysUserVo;
import com.jay.vito.uic.web.vo.WechatVo;
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
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public AuthResponse login(@RequestBody SysUser user) {
        SysUser loginUser = sysUserService.findByLoginName(user.getLoginName());
        if (Validator.isNull(loginUser)) {
            throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
        } else {
            if (MD5EncryptUtil.encrypt(user.getPassword()).equals(loginUser.getPassword())) {
                TokenData tokenData = new TokenData(loginUser.getId(), loginUser.getGroupId());
                tokenData.setManager(loginUser.manager());
                tokenData.setUicDomain(SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class));
//                tokenData.setAppDomains("");
                String token = TokenUtil.genToken(tokenData);
                AuthResponse authResp = new AuthResponse();
                authResp.setToken("bearer " + token);
                authResp.setUserId(loginUser.getId());
                authResp.setUserName(loginUser.getName());
                authResp.setManager(loginUser.manager());
                // 获取用户分配的应用及相关资源
                Set<String> resources = sysUserService.findUserResources(loginUser.getId());
                authResp.setResources(resources);
                return authResp;
            } else {
                throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
            }
        }
    }

    @IgnoreUserAuth
    @RequestMapping(value = "/api/wechat/login", method = RequestMethod.POST)
    public AuthResponse wechatLogin(@RequestBody String authCode) {
        // todo 通过authCode换取openid，并查询user表是否有相关记录 如果有生成token并返回
        String appid = "";
        String secret = "";
//        SnsToken snsToken = SnsAPI.oauth2AccessToken(appid, secret, authCode);
//        String openid = snsToken.getOpenid();
        String openid = "123456789";
        SysUser user = sysUserService.existsOpenId(openid);
        if (user != null) {
            TokenData tokenData = new TokenData(user.getId(), user.getGroupId());
            tokenData.setManager(user.manager());
            tokenData.setUicDomain(SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class));
            String token = TokenUtil.genToken(tokenData);
            AuthResponse authResp = new AuthResponse();
            authResp.setToken(token);
            authResp.setUserId(user.getId());
            authResp.setUserName(user.getName());
            authResp.setManager(user.manager());
            return authResp;
        } else {
            throw new HttpBadRequestException("非本平台用户，无权限", "INVALID_OPENID");
        }
    }

    @IgnoreUserAuth
    @RequestMapping(value = "/api/wechat/bind", method = RequestMethod.POST)
    public AuthResponse wechatBind(@RequestBody WechatVo wechatVo, HttpSession session) {
        // todo 传入手机号、openid、短信验证码   将openid与手机对应的用户关联起来
        String messageCode = String.valueOf(session.getAttribute("MessageCode"));
        String messageCode1 = wechatVo.getMessageCode();
        if (!messageCode.equals(messageCode1)) {
            throw new HttpBadRequestException("短信验证码填写错误", "INVALID_MESSAGE_CODE");
        }
        String mobile = wechatVo.getMobile();
        String openId = wechatVo.getOpenId();
        try {
            SysUser user = sysUserService.bind(mobile, openId);
            TokenData tokenData = new TokenData(user.getId(), user.getGroupId());
            tokenData.setManager(user.manager());
            tokenData.setUicDomain(SystemDataHolder.getParam(SystemParamKeys.UIC_DOMAIN, String.class));
            String token = TokenUtil.genToken(tokenData);
            AuthResponse authResp = new AuthResponse();
            authResp.setToken(token);
            authResp.setUserId(user.getId());
            authResp.setUserName(user.getName());
            authResp.setManager(user.manager());
            return authResp;
        } catch (Exception e) {
            throw new HttpBadRequestException(e.getMessage(), "FALID_BING_OPENID");
        }

    }

    /**
     * 获取一次性访问token  用于对接第三方系统登录
     *
     * @return
     */
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

    @IgnoreUserAuth
    @RequestMapping(value = "/api/forgetPwd", method = RequestMethod.POST)
    public boolean resetPwd(@RequestBody SysUserVo sysUserVo, HttpSession session) {
        // 验证手机号验证码 messageValidCode
        String validMessage = String.valueOf(session.getAttribute("MessageCode"));
        String messageValidCode = sysUserVo.getMessageValidCode();
        if (!validMessage.equals(messageValidCode)) {
            throw new HttpBadRequestException("验证码错误", "INVALID_MESSAGE_VALIDCODE");
        }
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUser, sysUserVo);
        boolean result = sysUserService.updatePwd(sysUser);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(MD5EncryptUtil.encrypt("admin@qzsdk2018"));
    }

}
