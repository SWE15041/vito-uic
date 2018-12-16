package com.jay.vito.uic.server.repository;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends MyJpaRepository<SysRole, Long> {

    SysRole findFirstByCodeAndGroupId(String code, Long groupId);

    List<SysRole> findAllByGroupIdAndIsDefault(Long groupId, YesNoEnum isDefault);
}