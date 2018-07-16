package com.jay.vito.uic.web.controller;

import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.domain.User;
import com.jay.vito.uic.service.UserService;
import com.jay.vito.website.core.exception.ErrorCodes;
import com.jay.vito.website.core.exception.HttpException;
import com.jay.vito.website.web.controller.BaseGridController;
import com.jay.vito.common.util.string.encrypt.MD5EncryptUtil;
import com.jay.vito.uic.web.vo.PwdModifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseGridController<User, Long> {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<User> query() {
        return super.query();
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "zx");
//        Page page = new Page();
//        page.setItems(userService.query(params));
//        return page;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable("id") Long id) {
        return super.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User save(@RequestBody User user) {
        return super.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable("id") Long id, @RequestBody User user) {
        user.setPassword(null);
        return super.update(id, user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

    @Override
    protected EntityCRUDService<User, Long> getEntityService() {
        return userService;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public User info() {
        return userService.get(UserContextHolder.getCurrentUserId());
    }

    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public User update(@RequestBody User user) {
        if (!UserContextHolder.getCurrentUserId().equals(user.getId())) {
            throw HttpException.of(ErrorCodes.POWERLESS_MODIFY);
        }
        user.setPassword(null);
        return super.update(UserContextHolder.getCurrentUserId(), user);
    }

    @RequestMapping(value = "/modifyPwd", method = RequestMethod.PUT)
    public Boolean modifyPwd(@RequestBody PwdModifyVo pwdModifyVo) {
        String origPwd = pwdModifyVo.getOrigPwd();
        User user = userService.get(UserContextHolder.getCurrentUserId());
        if (!user.getPassword().equals(MD5EncryptUtil.encrypt(origPwd))) {
            throw HttpException.of(ErrorCodes.INVALID_PASSWORD);
        }
        user.setPassword(MD5EncryptUtil.encrypt(pwdModifyVo.getNewPwd()));
        userService.update(user);
        return true;
    }
}
