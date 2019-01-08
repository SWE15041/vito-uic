package com.jay.vito.uic.server.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.server.domain.SysResource;
import com.jay.vito.uic.server.service.SysResourceService;
import com.jay.vito.uic.server.web.vo.ResourceNode;
import com.jay.vito.website.web.controller.BaseGridCRUDController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源接口控制器
 *
 * @author zhaixm
 * @date 2017/12/7 11:08
 */
@RestController
@RequestMapping("/resources")
public class SysResourceController extends BaseGridCRUDController<SysResource, Long, SysResourceService> {

	@Autowired
	private SysResourceService sysResourceService;

	/**
	 * 获取分配给用户的整个资源树
	 *
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<ResourceNode> getResourceTree() {
		//给当前用户分配相应权限范围的资源
		Long currentUserId = UserContextHolder.getCurrentUserId();
		List<SysResource> resources = sysResourceService.getUserResources(currentUserId);
		Map<Long, ResourceNode> resourceNodeMap = new HashMap<>();
		resources.forEach(resource -> {
			ResourceNode node = new ResourceNode();
			node.setId(resource.getId());
			node.setPid(resource.getParentId());
			node.setName(resource.getName());
			node.setSortNo(resource.getSortNo());
			node.setIcon(resource.getIcoName());
			node.setType(resource.getResourceType());
			resourceNodeMap.put(resource.getId(), node);
		});
		List<ResourceNode> rootNodes = toTree(resourceNodeMap);
		return rootNodes;
	}

	/**
	 * 转换为树结构
	 *
	 * @param resourceNodeMap
	 * @return
	 */
	private List<ResourceNode> toTree(Map<Long, ResourceNode> resourceNodeMap) {
		List<ResourceNode> rootNodes = new ArrayList<>();
		resourceNodeMap.entrySet().forEach(entry -> {
			ResourceNode node = entry.getValue();
			Long pid = node.getPid();
			if (Validator.isNotNull(pid)) {
				ResourceNode pNode = resourceNodeMap.get(pid);
				pNode.addChild(node);
			} else {
				rootNodes.add(node);
			}
		});
		return rootNodes;
	}

	/**
	 * 获取分配的菜单树
	 *
	 * @return
	 */
	@RequestMapping(value = "/menuTree", method = RequestMethod.GET)
	public List<ResourceNode> getMenuTree() {
		Long currentUserId = UserContextHolder.getCurrentUserId();
		List<SysResource> sysResources = sysResourceService.getUserMenus(currentUserId);
		Map<Long, ResourceNode> resourceNodeMap = new HashMap<>();
		sysResources.forEach(resource -> {
			ResourceNode node = new ResourceNode();
			node.setId(resource.getId());
			node.setPid(resource.getParentId());
			node.setName(resource.getName());
			node.setSortNo(resource.getSortNo());
			node.setUrl(resource.getUrl());
			node.setCode(resource.getCode());
			resourceNodeMap.put(resource.getId(), node);
		});
		List<ResourceNode> rootNode = toTree(resourceNodeMap);
		return rootNode;
	}

	/**
	 * 获取用户拥有的资源id
	 *
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/resourceIds", method = RequestMethod.GET)
	public List<Long> getResourceIds(@RequestParam Long userId) {
		List<Long> resourceIds = sysResourceService.getResourceIds(userId);
		return resourceIds;
	}
}
