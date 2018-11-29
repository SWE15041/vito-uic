package com.jay.vito.uic.server.repository;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.SysRoleResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleResourceRepository extends MyJpaRepository<SysRoleResource, Long> {

    List<SysRoleResource> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

}