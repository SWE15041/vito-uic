package com.jay.vito.uic.service.impl;

import com.jay.vito.uic.domain.RegApp;
import com.jay.vito.uic.domain.RegAppRepository;
import com.jay.vito.uic.service.RegAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 18:16
 * 描述:
 */
@Service
public class RegAppServiceImpl extends BusinessEntityCRUDServiceImpl<RegApp, Long> implements RegAppService {

    @Autowired
    private RegAppRepository regAppRepository;

    @Override
    protected JpaRepository<RegApp, Long> getRepository() {
        return regAppRepository;
    }

    @Override
    public List<RegApp> findAll() {
        return regAppRepository.findAll();
    }
}
