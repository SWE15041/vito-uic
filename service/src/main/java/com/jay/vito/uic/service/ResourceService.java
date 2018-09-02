package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.domain.SysResource;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 资源服务
 */
public interface ResourceService extends EntityCRUDService<SysResource, Long> {

    List<SysResource> findEnableResources();

}
