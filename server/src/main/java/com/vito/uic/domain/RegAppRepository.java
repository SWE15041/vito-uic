package com.vito.uic.domain;

import com.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegAppRepository extends MyJpaRepository<RegApp, Long> {

}