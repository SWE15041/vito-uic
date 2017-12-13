package com.vito.uic.web.controller;

import com.vito.storage.model.Page;
import com.vito.uic.domain.Role;
import com.vito.uic.service.RoleResourceService;
import com.vito.uic.service.RoleService;
import com.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController extends BaseGridController<Role> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<Role> query() {
        return pageQuery();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Role get(@PathVariable("id") Long id) {
        Role role = roleService.get(id);
        List<Long> roleResources = roleResourceService.findRoleResources(id);
        role.setResourceIds(new HashSet<>(roleResources));
        return role;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Role save(@RequestBody Role role) {
        return roleService.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Role update(@PathVariable("id") Long id, @RequestBody Role role) {
        return roleService.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        roleService.delete(id);
    }

}
