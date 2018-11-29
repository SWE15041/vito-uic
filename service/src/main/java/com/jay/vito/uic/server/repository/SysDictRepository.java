package com.jay.vito.uic.server.repository;

import com.jay.vito.storage.core.MyJpaRepository;
import com.jay.vito.uic.server.domain.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictRepository extends MyJpaRepository<SysDict, Long> {

    List<SysDict> findByName(String name);

}