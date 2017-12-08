package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleResourceRepository extends MyJpaRepository<RoleResource, Long> {

    List<RoleResource> findByRoleId(Long roleId);

}