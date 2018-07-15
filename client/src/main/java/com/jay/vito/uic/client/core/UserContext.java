package com.jay.vito.uic.client.core;


import com.jay.vito.common.util.bean.BeanUtil;
import com.jay.vito.uic.client.vo.User;

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
