package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;

@Repository
public interface SysRoleRepository extends MyJpaRepository<SysRole, Long> {

    SysRole findFirstByCode(String code);
    List<SysRole> findAllByGroupId(Long groupId);
}