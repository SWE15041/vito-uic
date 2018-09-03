package com.jay.vito.uic.service;

import com.jay.vito.uic.ApplicationTests;
import com.jay.vito.uic.domain.SysUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

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
        SysUser user = userService.findByLoginName("zxm2");
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId().longValue(), 1L);
    }

    @Test
    public void testFindUserResources() {
        SysUser user = userService.findByLoginName("zxm");
        Set<String> resourceCodes = userService.findUserResources(user.getId());
        System.out.println(resourceCodes);
    }

    @Test
    public void testGetUser() {
        SysUser user = userService.get(1L);
        System.out.println(user.getRoleIds());
        Assert.assertNotNull(user.getRoleIds());
    }
}
