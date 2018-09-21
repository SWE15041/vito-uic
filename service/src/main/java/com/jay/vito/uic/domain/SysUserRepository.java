package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends MyJpaRepository<SysUser, Long> {

    SysUser findByLoginName(String loginName);

    boolean existsByMobile(String mobile);

    boolean existsByLoginName(String loginName);

    SysUser findByMobile(String mobile);
}