package com.jay.vito.uic.server.repository;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.constant.ResourceType;
import com.jay.vito.uic.server.domain.SysResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysResourceRepository extends MyJpaRepository<SysResource, Long> {

    List<SysResource> findByEnable(YesNoEnum enable);

    SysResource findFirstByIdAndEnable(Long id, YesNoEnum enable);

    List<SysResource> findByEnableAndResourceType(YesNoEnum enable, ResourceType resourceType);

}