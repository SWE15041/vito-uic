package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends MyJpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

}