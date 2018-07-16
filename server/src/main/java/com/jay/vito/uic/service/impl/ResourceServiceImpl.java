package com.jay.vito.uic.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.uic.domain.Resource;
import com.jay.vito.uic.domain.ResourceRepository;
import com.jay.vito.uic.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class ResourceServiceImpl extends EntityCRUDServiceImpl<Resource, Long> implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    protected JpaRepository<Resource, Long> getRepository() {
        return resourceRepository;
    }

    @Override
    public List<Resource> findEnableResources() {
        List<Resource> resources = resourceRepository.findByEnable(YesNoEnum.YES);
        return resources;
    }
}
