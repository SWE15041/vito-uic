package com.jay.vito.uic.web.controller;

import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.domain.SysUser;
import com.jay.vito.uic.service.UserService;
import com.jay.vito.uic.web.vo.PwdModifyVo;
import com.jay.vito.website.core.exception.ErrorCodes;
import com.jay.vito.website.core.exception.HttpException;
import com.jay.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseGridController<SysUser, Long> {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<SysUser> query() {
        return super.query();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SysUser get(@PathVariable("id") Long id) {
        return super.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public SysUser save(@RequestBody SysUser user) {
        return super.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SysUser update(@PathVariable("id") Long id, @RequestBody SysUser user) {
        user.setPassword(null);
        return super.update(id, user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

    @Override
    protected EntityCRUDService<SysUser, Long> getEntityService() {
        return userService;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public SysUser info() {
        return userService.get(UserContextHolder.getCurrentUserId());
    }

    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public SysUser update(@RequestBody SysUser user) {
        if (!UserContextHolder.getCurrentUserId().equals(user.getId())) {
            throw HttpException.of(ErrorCodes.POWERLESS_MODIFY);
        }
        user.setPassword(null);
        return super.update(UserContextHolder.getCurrentUserId(), user);
    }

    @RequestMapping(value = "/modifyPwd", method = RequestMethod.PUT)
    public Boolean modifyPwd(@RequestBody PwdModifyVo pwdModifyVo) {
        String origPwd = pwdModifyVo.getOrigPwd();
        SysUser user = userService.get(UserContextHolder.getCurrentUserId());
        if (!user.getPassword().equals(MD5EncryptUtil.encrypt(origPwd))) {
            throw HttpException.of(ErrorCodes.INVALID_PASSWORD);
        }
        user.setPassword(MD5EncryptUtil.encrypt(pwdModifyVo.getNewPwd()));
        userService.update(user);
        return true;
    }

}
