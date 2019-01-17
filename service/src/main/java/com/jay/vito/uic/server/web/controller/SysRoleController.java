package com.jay.vito.uic.server.web.controller;

import com.jay.vito.storage.model.Page;
import com.jay.vito.uic.server.domain.SysRole;
import com.jay.vito.uic.server.service.SysRoleResourceService;
import com.jay.vito.uic.server.service.SysRoleService;
import com.jay.vito.website.web.controller.BaseGridCRUDController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

/**
 * 角色接口控制器
 *
 * @author zhaixm
 * @date 2017/11/23 18:18
 */
@RestController
@RequestMapping("/roles")
public class SysRoleController extends BaseGridCRUDController<SysRole, Long, SysRoleService> {

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    @Override
    public Page<SysRole> query() {
        return super.query();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SysRole> queryAll() {
        return entityService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Override
    public SysRole get(@PathVariable("id") Long id) {
        SysRole role = entityService.get(id);
        List<Long> roleResources = sysRoleResourceService.findRoleResources(id);
        role.setResourceIds(new HashSet<>(roleResources));
        return role;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Override
    public SysRole save(@RequestBody SysRole role) {
        return entityService.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Override
    public SysRole update(@PathVariable("id") Long id, @RequestBody SysRole role) {
        role.setId(id);
        return entityService.updateNotNull(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Override
    public Boolean delete(@PathVariable("id") Long id) {
        entityService.delete(id);
        return true;
    }

}
