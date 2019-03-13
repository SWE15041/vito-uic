package com.jay.vito.uic.web.controller;

import com.jay.vito.storage.model.Page;
import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.client.core.UserContextHolder;
import com.jay.vito.uic.domain.SysResource;
import com.jay.vito.uic.service.SysResourceService;
import com.jay.vito.uic.web.vo.ResourceNode;
import com.jay.vito.website.web.controller.BaseGridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<ResourceNode> rootNodes = sysResourceService.sortResources(resources);
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
        List<ResourceNode> rootNode = sysResourceService.sortResources(sysResources);
        return rootNode;
    }

    @RequestMapping(value = "/resourceIds", method = RequestMethod.GET)
    public List<Long> getResourceIds(@RequestParam Long userId) {
        List<Long> resourceIds = sysResourceService.getResourceIds(userId);
        return resourceIds;
    }

    @RequestMapping(value = "/allResources", method = RequestMethod.GET)
    public List<ResourceNode> getAllResources() {
        List<SysResource> allResources = sysResourceService.getAllResources();
        List<ResourceNode> nodes = sysResourceService.sortResources(allResources);
        return nodes;
    }

}
