package com.jay.vito.uic.server.repository;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.RegApp;
import org.springframework.stereotype.Repository;

@Repository
public interface RegAppRepository extends MyJpaRepository<RegApp, Long> {

}