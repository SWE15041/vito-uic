package com.jay.vito.uic.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.vito.uic.domain.Resource;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 资源服务
 */
public interface ResourceService extends EntityCRUDService<Resource, Long> {

    List<Resource> findEnableResources();

}
