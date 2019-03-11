package com.jay.vito.uic.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.constant.ResourceType;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.service.SysResourceService;
import com.jay.vito.uic.web.vo.ResourceNode;
import com.jay.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/12/7 11:08
 * 描述: 资源控制器
 */
@RestController
@RequestMapping("/api/resources")
public class SysResourceController extends BaseGridController<SysResource, Long> {

    @Autowired
    private SysResourceService sysResourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<SysResource> query() {
        return super.query();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SysResource get(@PathVariable Long id) {
        return super.get(id);
    }

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

    @RequestMapping(method = RequestMethod.POST)
    public SysResource save(@RequestBody SysResource resource) {
        return super.save(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public SysResource update(@PathVariable("id") Long id, @RequestBody SysResource resource) {
        return super.update(id, resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Boolean delete(@PathVariable("id") Long id) {
        return super.delete(id);
    }

    @Override
    protected EntityCRUDService<SysResource, Long> getEntityService() {
        return sysResourceService;
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

    @RequestMapping(value = "/allResources", method = RequestMethod.GET)
    public List<SysResource> getAllResources(@RequestParam Long userId) {
        return sysResourceService.getAllResources();
    }
}
