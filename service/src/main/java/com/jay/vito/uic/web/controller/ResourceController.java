package com.jay.vito.uic.web.controller;

import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.constant.ResourceType;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.service.ResourceService;
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
public class ResourceController extends BaseGridController<SysResource, Long> {

    @Autowired
    private ResourceService resourceService;

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
        List<SysResource> resources = resourceService.findEnableResources();
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
        return resourceService;
    }
}
