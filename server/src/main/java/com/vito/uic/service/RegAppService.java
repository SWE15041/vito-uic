package com.vito.uic.service;

import com.vito.storage.service.EntityCRUDService;
import com.vito.uic.domain.RegApp;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 注册应用服务
 */
public interface RegAppService extends EntityCRUDService<RegApp, Long> {

    List<RegApp> findAll();

}
