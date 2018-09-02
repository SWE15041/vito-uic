package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleResourceRepository extends MyJpaRepository<RoleResource, Long> {

    List<RoleResource> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

}