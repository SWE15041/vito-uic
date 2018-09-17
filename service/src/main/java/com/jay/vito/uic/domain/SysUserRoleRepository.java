package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends MyJpaRepository<SysUserRole, Long> {

    List<SysUserRole> findByUserId(Long userId);

    void deleteByUserId(Long roleId);
}