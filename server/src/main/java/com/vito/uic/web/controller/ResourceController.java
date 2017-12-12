package com.vito.uic.web.controller;

import com.vito.common.util.validate.Validator;
import com.vito.storage.model.Page;
import com.vito.uic.domain.Resource;
import com.vito.uic.service.ResourceService;
import com.vito.uic.web.vo.ResourceNode;
import com.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/12/7 11:08
 * 描述:
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController extends BaseGridController<Resource> {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(method = RequestMethod.GET, params = {"pageNo"})
    public Page<Resource> query() {
        return pageQuery();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Resource get(@PathVariable Long id) {
        return resourceService.get(id);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ResourceNode> getResourceTree() {
        List<Resource> resources = resourceService.findEnableResources();
        Map<Long, ResourceNode> resourceNodeMap = new HashMap<>();
        resources.forEach(resource -> {
            ResourceNode node = new ResourceNode();
            node.setId(resource.getId());
            node.setPid(resource.getParentId());
            node.setName(resource.getName());
            node.setSortNo(resource.getSortNo());
            String icon = "glyphicon glyphicon-th-large";
            switch (resource.getResourceType()) {
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
    public Resource save(@RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Resource update(@PathVariable("id") Long id, @RequestBody Resource resource) {
        return resourceService.save(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        resourceService.delete(id);
    }

}
