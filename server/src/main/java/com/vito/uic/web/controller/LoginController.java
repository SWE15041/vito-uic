package com.vito.uic.web.controller;

import com.vito.common.util.validate.Validator;
import com.vito.uic.client.core.TokenData;
import com.vito.uic.client.core.TokenUtil;
import com.vito.uic.domain.User;
import com.vito.uic.service.UserService;
import com.vito.uic.web.vo.AuthResponse;
import com.vito.website.core.cache.SystemDataHolder;
import com.vito.website.core.cache.SystemParamKeys;
import com.vito.website.core.exception.ErrorCodes;
import com.vito.website.core.exception.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:18
 * 描述: 认证控制器
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 登录验证
     *
     * @param user
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public AuthResponse login(@RequestBody User user) {
        User loginUser = userService.findByLoginName(user.getLoginName());
        if (Validator.isNull(loginUser)) {
            throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
        } else {
            if (loginUser.getPassword().equals(user.getPassword())) {
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
                Set<String> resources = userService.findUserResources(loginUser.getId());
                authResp.setResources(resources);
                return authResp;
            } else {
                throw HttpException.of(ErrorCodes.INVALID_USERNAME_PASSWORD);
            }
        }
    }

}
