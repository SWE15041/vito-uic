package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleRepository extends MyJpaRepository<SysRole, Long> {

    SysRole findFirstByCodeAndGroupId(String code,Long groupId);

    List<SysRole> findAllByGroupId(Long groupId);
}