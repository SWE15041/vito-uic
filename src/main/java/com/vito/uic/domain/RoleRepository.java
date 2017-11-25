package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MyJpaRepository<Role, Long> {

}