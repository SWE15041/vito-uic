package com.vito.uic.client;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.vito.common.util.json.JsonParser;
import com.vito.common.util.validate.Validator;
import com.vito.common.util.web.WebUtil;
import com.vito.uic.client.model.AuthResponse;
import com.vito.uic.client.model.User;
import com.vito.uic.client.model.UserContext;
import com.vito.uic.client.support.HttpUtil;
import com.vito.uic.client.support.UserContextUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 作者: zhaixm
 * 日期: 2017/11/26 0:07
 * 描述: UIC用户认证client filter
 */
public abstract class UICAuthFilter implements Filter {

    //todo 此参数要提供配置方法
    private String authWebUrl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        String serviceTicket = httpReq.getParameter("st");
        if (Validator.isNotNull(serviceTicket)) {
            AuthResponse authResponse = doAuth(serviceTicket);
            if (authResponse.getResult()) {
                try {
                    User user = authResponse.getUser();
                    Long userId = user.getId();
                    Algorithm algorithm = Algorithm.HMAC256("secret");
                    String jwt = JWT.create()
                                    .withIssuer(userId.toString())
                                    .sign(algorithm);
                    UserContextUtil.addUser(jwt, user);
                    // 不使用tomcat的session机制，自己保存会话  方便系统进行分布式部署
                    httpResp.addCookie(new Cookie("ut", jwt));
                } catch (UnsupportedEncodingException exception) {
                    //UTF-8 encoding not supported
                } catch (JWTCreationException exception) {
                    //Invalid Signing configuration / Couldn't convert Claims.
                }
            } else {
                httpResp.getWriter().write("用户认证失败");
            }
        } else {
            Cookie utCookie = WebUtil.getCookie("ut", httpReq);
            String jwt = utCookie.getValue();
            if (Validator.isNotNull(jwt)) {
                UserContext userContext = UserContextUtil.getUserContext(jwt);
                if (Validator.isNotNull(userContext)) {
                    UserContextUtil.setUserContext(userContext);
                }
            }
        }
        User curUser = UserContextUtil.getUser();
        //todo 可以设置不需过滤的路径
        if (Validator.isNull(curUser)) {
            // 转向uic的登录页面
            httpResp.sendRedirect(authWebUrl);
        }
        // 已有登录用户 继续执行后续方法
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private AuthResponse doAuth(String st) {
        JSONObject param = new JSONObject();
        param.put("serviceTicket", st);
        String resp = HttpUtil.postJson(authWebUrl + "/auth", param.toJSONString());
        AuthResponse authResponse = JsonParser.parseJson2Obj(resp, AuthResponse.class);
        return authResponse;
    }

    public User getSessionUser() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
