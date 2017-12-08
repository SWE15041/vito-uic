package com.vito.uic.domain;

import com.vito.common.model.enums.YesNoEnum;
import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends MyJpaRepository<Resource, Long> {

    List<Resource> findByEnable(YesNoEnum enable);

}