package com.jay.vito.uic.server.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.server.domain.SysDict;
import com.jay.vito.uic.server.service.SysDictService;
import com.jay.vito.website.web.controller.BaseGridCRUDController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典接口控制器
 *
 * @author zhaixm
 * @date 2018-12-16
 */
@RestController
@RequestMapping(value = "/dicts")
public class SysDictController extends BaseGridCRUDController<SysDict, Long, SysDictService> {

	@RequestMapping(method = RequestMethod.GET)
	public List<SysDict> queryAll(@RequestParam String name) {
		if (Validator.isNotNull(name)) {
			return entityService.findByName(name);
		} else {
			return entityService.getAll();
		}
	}

}
