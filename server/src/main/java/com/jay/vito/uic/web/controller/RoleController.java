package com.jay.vito.uic.web.controller;

import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.Role;
import com.jay.vito.uic.service.RoleResourceService;
import com.jay.vito.uic.service.RoleService;
import com.jay.vito.website.web.controller.BaseGridController;
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
public class RoleController extends BaseGridController<Role, Long> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourceService roleResourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<Role> query() {
        return super.query();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Role> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Role get(@PathVariable("id") Long id) {
        Role role = super.get(id);
        List<Long> roleResources = roleResourceService.findRoleResources(id);
        role.setResourceIds(new HashSet<>(roleResources));
        return role;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Role save(@RequestBody Role role) {
        return super.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Role update(@PathVariable("id") Long id, @RequestBody Role role) {
        return super.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

    @Override
    protected EntityCRUDService<Role, Long> getEntityService() {
        return roleService;
    }
}
