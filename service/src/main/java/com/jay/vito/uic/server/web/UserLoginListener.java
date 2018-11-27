package com.jay.vito.uic.server.web;

import com.jay.vito.uic.client.core.UserContext;
import com.jay.vito.uic.server.domain.SysUser;

/**
 * 作者: zhaixm
 * 日期: 2018/9/2 23:04
 * 描述:
 */
public interface UserLoginListener {

    void afterLogin(SysUser user, UserContext userContext);

}
