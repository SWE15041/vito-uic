package com.jay.vito.uic.server.repository;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.SysUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends MyJpaRepository<SysUser, Long> {

	SysUser findFirstByLoginNameAndGroupId(String loginName, Long groupId);

	SysUser findFirstByMobileAndGroupId(String mobile, Long groupId);

	boolean existsByMobileAndGroupId(String mobile, Long groupId);

	boolean existsByLoginNameAndGroupId(String loginName, Long groupId);

}