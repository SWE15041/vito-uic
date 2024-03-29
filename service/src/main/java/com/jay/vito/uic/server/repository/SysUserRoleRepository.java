package com.jay.vito.uic.server.repository;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.SysUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends MyJpaRepository<SysUserRole, Long> {

    List<SysUserRole> findByUserId(Long userId);

    void deleteByUserIdAndGroupId(Long roleId,Long groupId);

    List<SysUserRole> findByUserIdAndGroupId(Long userId,Long groupId);
}