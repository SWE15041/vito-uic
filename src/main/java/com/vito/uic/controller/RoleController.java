package com.vito.uic.controller;

import com.vito.storage.model.Page;
import com.vito.uic.domain.Role;
import com.vito.uic.service.RoleService;
import com.vito.website.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/roles")
public class RoleController extends BaseGridController<Role> {

    @Autowired
    private RoleService roleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Role> query() {
        return pageQuery();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Role save(@RequestBody Role user) {
        return roleService.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Role update(@PathVariable("id") Long id, @RequestBody Role user) {
        return roleService.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        roleService.delete(id);
    }

}
