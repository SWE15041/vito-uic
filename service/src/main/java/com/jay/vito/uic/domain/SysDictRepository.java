package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictRepository extends MyJpaRepository<SysDict, Long> {


}