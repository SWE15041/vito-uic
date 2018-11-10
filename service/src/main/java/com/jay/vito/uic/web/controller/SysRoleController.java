package com.jay.vito.uic.web.controller;

import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysRole;
import com.jay.vito.uic.service.SysRoleResourceService;
import com.jay.vito.uic.service.SysRoleService;
import com.jay.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:18
 * 描述:
 */
@RestController
@RequestMapping("/api/roles")
public class SysRoleController extends BaseGridController<SysRole, Long> {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<SysRole> query() {
        return super.query();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SysRole> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SysRole get(@PathVariable("id") Long id) {
        SysRole role = super.get(id);
        List<Long> roleResources = sysRoleResourceService.findRoleResources(id);
        role.setResourceIds(new HashSet<>(roleResources));
        return role;
    }

    @RequestMapping(method = RequestMethod.POST)
    public SysRole save(@Valid @RequestBody SysRole role, BindingResult result) {
        ValidUtil.valid(result);
        return super.save(role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SysRole update(@PathVariable("id") Long id, @Valid @RequestBody SysRole role, BindingResult result) {
        ValidUtil.valid(result);
        return super.update(id, role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

    @Override
    protected EntityCRUDService<SysRole, Long> getEntityService() {
        return sysRoleService;
    }
}
