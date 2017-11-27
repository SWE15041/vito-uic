package com.vito.uic.controller;

import com.vito.common.model.Pair;
import com.vito.common.util.string.CodeGenerateUtil;
import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.uic.controller.vo.AuthRequest;
import com.vito.uic.controller.vo.AuthResponse;
import com.vito.uic.domain.User;
import com.vito.uic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:18
 * 描述:
 */
@Controller
public class AuthController {

    private Map<String, User> stUserCache = Collections.synchronizedMap(new HashMap<>());
    private Map<String, Pair<User, Date>> tgcUserCache = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(String target, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            response.addCookie(new Cookie("tgc", "test"));
//            response.sendRedirect("http://www.baidu.com");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        //todo check request cookie 直接验证tgc是否合法，生成serviceTicket返回给应用
        Cookie ticketGrantCookie = WebUtil.getCookie("tgc", request);
        String ticketGrantVal = ticketGrantCookie.getValue();
        if (Validator.isNotNull(ticketGrantVal)) {
            Pair<User, Date> userDatePair = tgcUserCache.get(ticketGrantVal);
            User loginUser = userDatePair.getFirst();
            String st = genServiceTicket(loginUser);
            redirect(target, st, response);
        }
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
        if (Validator.isNull(target)) {
            // todo 必须有目标地址
        }
        User loginUser = userService.findByLoginName(loginName);
        if (loginUser.getPassword().equals(password)) {
            String serviceTicket = genServiceTicket(loginUser);
            String ticketGrantVal = CodeGenerateUtil.generateUUID();
            Pair<User, Date> userDatePair = new Pair<>(loginUser, new Date());
            tgcUserCache.put(ticketGrantVal, userDatePair);
            // 向客户端浏览器设置tgc，以便浏览器跳转用户中心管理的新应用时无需再进行登录即可通过认证
            response.addCookie(new Cookie("tgc", ticketGrantVal));
            redirect(target, serviceTicket, response);

        } else {
            //TODO 抛出特定的http错误
            throw new RuntimeException("");
        }
    }

    private void redirect(String target, String serviceTicket, HttpServletResponse response) {
        try {
            String redirectUrl = target.indexOf("?") > -1 ? target + "&st=" + serviceTicket : target + "?st=" + serviceTicket;
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String genServiceTicket(User loginUser) {
        String serviceTicket = CodeGenerateUtil.generateUUID();
        stUserCache.put(serviceTicket, loginUser);
        return serviceTicket;
    }

    /**
     * 通过st(serviceTicket)来验证是否已通过合法认证登录，如果验证通过则返回用户信息
     * 该接口有uic-client中的uicAuthFilter访问
     *
     * @param authRequest
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public AuthResponse auth(@RequestBody AuthRequest authRequest) {
        String serviceTicket = authRequest.getServiceTicket();
        User authUser = stUserCache.get(serviceTicket);
        if (Validator.isNotNull(authUser)) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setResult(true);
            authResponse.setUser(authUser);
            stUserCache.remove(serviceTicket); //一旦st使用过就马上清理
            return authResponse;
        } else {
            //todo throw ticket invalid exception
            throw new RuntimeException("");
        }
    }

}
