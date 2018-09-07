package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleResourceRepository extends MyJpaRepository<SysRoleResource, Long> {

    List<SysRoleResource> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);
}