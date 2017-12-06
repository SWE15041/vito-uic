package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends MyJpaRepository<Resource, Long> {

}