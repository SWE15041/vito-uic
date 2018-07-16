package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.common.model.enums.YesNoEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MyJpaRepository<Resource, Long> {

    List<Resource> findByEnable(YesNoEnum enable);

}