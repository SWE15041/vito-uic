package com.vito.uic.web.controller;

import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.uic.domain.User;
import com.vito.uic.service.UserService;
import com.vito.uic.web.vo.AuthRequest;
import com.vito.uic.web.vo.AuthResponse;
import com.vito.website.constant.SessionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vito.uic.web.support.UserTicketCache.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/25 23:18
 * 描述: 认证控制器
 */
@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(String target, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
        Cookie ticketGrantCookie = WebUtil.getCookie("tgc", request);
        if (Validator.isNotNull(ticketGrantCookie)) {
            String ticketGrantVal = ticketGrantCookie.getValue();
            if (Validator.isNotNull(ticketGrantVal)) {
                // 验证tgc是否合法，生成serviceTicket返回给应用
                User authUser = getTgcUser(ticketGrantVal);
                if (Validator.isNotNull(authUser)) {
                    String st = genServiceTicket(authUser);
                    redirect(target, st, response);
                }
            }
        }
        modelMap.put("target", target);
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
    public String login(String loginName,
                        String password,
                        String target,
                        ModelMap modelMap,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        modelMap.put("target", target);
        if (Validator.isNull(target)) {
            modelMap.put("errMsg", "访问方式不合法！");
            return "invalid";
        }
        User loginUser = userService.findByLoginName(loginName);
        if (Validator.isNull(loginUser)) {
            modelMap.put("errMsg", "用户名不存在");
        } else {
            if (loginUser.getPassword().equals(password)) {
                String serviceTicket = genServiceTicket(loginUser);
                String ticketGrantCookie = genTicketGrantCookie(loginUser);
                request.getSession().setAttribute(SessionConstant.USER, loginUser);
                // 向客户端浏览器设置tgc，以便浏览器跳转用户中心管理的新应用时无需再进行登录即可通过认证
                response.addCookie(new Cookie("tgc", ticketGrantCookie));
                redirect(target, serviceTicket, response);
            } else {
                //TODO 抛出特定的http错误
//                throw new RuntimeException("");
                modelMap.put("errMsg", "用户名密码错误");
            }
        }
        return "login";
    }

    private void redirect(String target, String serviceTicket, HttpServletResponse response) {
        try {
            String redirectUrl = target.indexOf("?") > -1 ? target + "&st=" + serviceTicket : target + "?st=" + serviceTicket;
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        AuthResponse authResponse = new AuthResponse();
        String serviceTicket = authRequest.getServiceTicket();
        User authUser = getStUser(serviceTicket);
//        authUser = new User();
//        authUser.setLoginName("zxm");
//        authUser.setName("zhaixm");
//        authResponse.setRoleCodes(new ImmutableSet.Builder<String>().add("ORDER", "STALL").build());
        if (Validator.isNotNull(authUser)) {
            authResponse.setResult(true);
            authResponse.setUser(authUser);
        } else {
            authResponse.setResult(false);
            authResponse.setErrMsg("令牌无效，请重新获取授权");
        }
        logger.debug("认证结果{}", authResponse.getResult());
        return authResponse;
    }

}
