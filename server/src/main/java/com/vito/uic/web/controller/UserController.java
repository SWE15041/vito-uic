package com.vito.uic.web.controller;

import com.vito.storage.model.Page;
import com.vito.storage.service.EntityCRUDService;
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
}
