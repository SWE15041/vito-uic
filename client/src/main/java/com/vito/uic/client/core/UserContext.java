package com.vito.uic.client.core;


import com.vito.common.util.bean.BeanUtil;
import com.vito.uic.client.model.User;

/**
 * 作者: zhaixm
 * 日期: 2017/12/3 23:30
 * 描述:
 */
public class UserContext extends TokenData {

    private User user;

    public UserContext(TokenData tokenData) {
        BeanUtil.copyProperties(this, tokenData);
    }

    public User getUser() {
        if (user == null) {
            //todo 从认证服务器获取该用户相关信息
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
