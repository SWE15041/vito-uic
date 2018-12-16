package com.jay.vito.uic.server.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.server.constant.ResourceType;
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

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ResourceNode> getResourceTree() {
        //给当前用户分配相应权限范围的资源
        Long currentUserId = UserContextHolder.getCurrentUserId();
        List<SysResource> resources = sysResourceService.getUserResources(currentUserId);

//        //获取可用资源
//        List<SysResource> resources = sysResourceService.findEnableResources();

        Map<Long, ResourceNode> resourceNodeMap = new HashMap<>();
        resources.forEach(resource -> {
            ResourceNode node = new ResourceNode();
            node.setId(resource.getId());
            node.setPid(resource.getParentId());
            node.setName(resource.getName());
            node.setSortNo(resource.getSortNo());
            String icon = "glyphicon glyphicon-th-large";
            ResourceType resourceType = Validator.isNotNull(resource.getResourceType()) ? resource.getResourceType() : ResourceType.FUNC;
            switch (resourceType) {
                case MODULE:
                    icon = "glyphicon glyphicon-book";
                    break;
                case FUNC:
                    icon = "glyphicon glyphicon-cog";
                    break;
            }
            node.setIcon(icon);
            resourceNodeMap.put(resource.getId(), node);
        });
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

    @RequestMapping(value = "/menuTree", method = RequestMethod.GET)
    public List<ResourceNode> getResourceTreeByRole() {

        Long currentUserId = UserContextHolder.getCurrentUserId();
        List<SysResource> sysResources = sysResourceService.getUserMenu(currentUserId);

        Map<Long, ResourceNode> resourceNodeMap2 = new HashMap<>();
        sysResources.forEach(resource2 -> {
            ResourceNode node = new ResourceNode();
            node.setId(resource2.getId());
            node.setPid(resource2.getParentId());
            node.setName(resource2.getName());
            node.setSortNo(resource2.getSortNo());
            node.setUrl(resource2.getUrl());
            node.setCode(resource2.getCode());
            resourceNodeMap2.put(resource2.getId(), node);
        });

        List<ResourceNode> rootNode = new ArrayList<>();
        resourceNodeMap2.entrySet().forEach(entry -> {
            ResourceNode node = entry.getValue();
            Long pid = node.getPid();
            if (Validator.isNotNull(pid)) {
                ResourceNode pNode = resourceNodeMap2.get(pid);
                pNode.addChild(node);
            } else {
                rootNode.add(node);
            }
        });
        return rootNode;
    }

    @RequestMapping(value = "/resourceIds", method = RequestMethod.GET)
    public List<Long> getResourceIds(@RequestParam Long userId) {
        List<Long> resourceIds = sysResourceService.getResourceIds(userId);
        return resourceIds;
    }
}
