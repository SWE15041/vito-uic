package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MyJpaRepository<User, Long> {

    User findByLoginName(String loginName);

}