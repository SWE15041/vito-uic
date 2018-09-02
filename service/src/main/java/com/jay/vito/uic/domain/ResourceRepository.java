package com.jay.vito.uic.domain;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MyJpaRepository<SysResource, Long> {

    List<SysResource> findByEnable(YesNoEnum enable);

}