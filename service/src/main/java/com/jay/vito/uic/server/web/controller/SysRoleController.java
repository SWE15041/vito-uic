package com.jay.vito.uic.server.web.controller;

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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Override
	public SysRole get(@PathVariable("id") Long id) {
		SysRole role = entityService.get(id);
		List<Long> roleResources = sysRoleResourceService.findRoleResources(id);
		role.setResourceIds(new HashSet<>(roleResources));
		return role;
	}

	@GetMapping
	public List<SysRole> queryAll() {
		return entityService.getAll();
	}

}
