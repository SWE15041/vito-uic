package com.vito.uic.controller;

import com.vito.common.util.string.CodeGenerateUtil;
import com.vito.uic.controller.vo.AuthRequest;
import com.vito.uic.controller.vo.AuthResponse;
import com.vito.uic.domain.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:18
 * 描述:
 */
public class AuthController {

    private Map<String, User> authResultCache = Collections.synchronizedMap(new HashMap<>());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 登录验证
     *
     * @param loginName
     * @param password
     * @param target
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(String loginName,
                      String password,
                      String target,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        User user = new User();
        user.setName("zz");
        user.setLoginName("zxm");
        String serviceTicket = CodeGenerateUtil.generateUUID();
        authResultCache.put(serviceTicket, user);
        try {
            response.addCookie(new Cookie("tgc", "test"));
            response.sendRedirect(target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过st(serviceTicket)和tgc(ticketGrantCookie)来验证是否已通过合法认证登录，如果验证通过则返回用户信息
     * 该接口有uic-client中的uicAuthFilter访问
     *
     * @param authRequest
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        String serviceTicket = authRequest.getServiceTicket();
        User authUser = authResultCache.get(serviceTicket);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(true);
        authResponse.setUser(authUser);
        return authResponse;
    }

}
