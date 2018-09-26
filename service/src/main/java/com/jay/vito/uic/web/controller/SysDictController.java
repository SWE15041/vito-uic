package com.jay.vito.uic.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysDict;
import com.jay.vito.uic.service.SysDictService;
import com.jay.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dicts")
public class SysDictController extends BaseGridController<SysDict, Long> {


    @Autowired
    private SysDictService sysDictService;

    @Override
    protected EntityCRUDService<SysDict, Long> getEntityService() {
        return sysDictService;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<SysDict> query() {
        return super.query();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SysDict> getAll(@RequestParam String name) {
        if (Validator.isNotNull(name)) {
            return sysDictService.findByName(name);
        } else {
            return super.getAll();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public SysDict save(@RequestBody SysDict sysDict) {
        return super.save(sysDict);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SysDict update(@PathVariable("id") Long id, @RequestBody SysDict sysDict) {
        return super.update(id, sysDict);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

}
