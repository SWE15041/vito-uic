package com.jay.vito.uic.server.service.impl;

import com.jay.vito.storage.service.EntityCRUDServiceImpl;
import com.jay.vito.uic.server.domain.SysDict;
import com.jay.vito.uic.server.repository.SysDictRepository;
import com.jay.vito.uic.server.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class SysDictServiceImpl extends EntityCRUDServiceImpl<SysDict, Long> implements SysDictService {

    @Autowired
    private SysDictRepository sysDictRepository;

    @Override
    public List<SysDict> findByName(String name) {
        return sysDictRepository.findByName(name);
    }
}
