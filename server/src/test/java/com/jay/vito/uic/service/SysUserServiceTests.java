package com.jay.vito.uic.service;

import com.jay.vito.uic.ApplicationTests;
import com.jay.vito.uic.server.domain.SysUser;
import com.jay.vito.uic.server.service.SysUserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 0:01
 * 描述:
 */
public class SysUserServiceTests extends ApplicationTests {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void testFindByLoginName() {
        SysUser user = sysUserService.findByLoginName("zxm2");
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getId().longValue(), 1L);
    }

    @Test
    public void testFindUserResources() {
        SysUser user = sysUserService.findByLoginName("zxm");
        Set<String> resourceCodes = sysUserService.findUserResources(user.getId());
        System.out.println(resourceCodes);
    }

    @Test
    public void testGetUser() {
        SysUser user = sysUserService.get(1L);
        System.out.println(user.getRoleIds());
        Assert.assertNotNull(user.getRoleIds());
    }
}
