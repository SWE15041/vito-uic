package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends MyJpaRepository<UserRole, Long> {

}