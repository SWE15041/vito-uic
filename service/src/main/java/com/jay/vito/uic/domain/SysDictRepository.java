package com.jay.vito.uic.domain;

import com.jay.vito.storage.core.MyJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysDictRepository extends MyJpaRepository<SysDict, Long> {

    List<SysDict> findByName(String name);

}