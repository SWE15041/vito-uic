package com.vito.uic.web.controller;

import com.vito.storage.model.Page;
import com.vito.uic.domain.User;
import com.vito.uic.service.UserService;
import com.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseGridController<User> {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo", "pageSize"})
    public Page<User> query() {
        logger.debug("log4j test**************");
        return pageQuery();
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "zx");
//        Page page = new Page();
//        page.setItems(userService.query(params));
//        return page;
    }

    @RequestMapping(method = RequestMethod.POST)
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}
