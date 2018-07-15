/**
 * 文件名: SessionConstant.java
 * 作者: zhaixm
 * 版本: 2014-11-6 下午4:48:11 v1.0
 * 日期: 2014-11-6
 * 描述:
 */
package com.jay.vito.website.constant;

/**
 * 作者: zhaixm
 * 日期: 2014-11-6 下午4:48:11 
 * 描述: 存放在session中的参数名静态值
 */
public class SessionConstant {

    /**
     * 存放在session中的用户名称
     */
    public static final String USER = "_sessionUser";

    /**
     * 存放在session中的用户名称
     */
    public static final String SUPER_USER = "_sessionSuperUser";

    /**
     * 存放在session中的验证码名称
     */
    public static final String VCODE = "_sessionVCode";

    /**
     * 用户登录前想访问的真实地址
     */
    public static final String TARGET_REQUEST_URL = "_sessionTargetReqUrl";

    /**
     * 用于自动登录的cookie令牌名称
     */
    public static final String COOKIE_AUTO_LOGIN_TOKEN = "_attoken";

}
