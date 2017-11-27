package com.vito.uic.service;

import com.vito.uic.ApplicationTests;
import com.vito.uic.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 0:01
 * 描述:
 */
public class UserServiceTests extends ApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void testFindByLoginName() {
        User user = userService.findByLoginName("zxm2");
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId().longValue(), 1L);
    }
}
