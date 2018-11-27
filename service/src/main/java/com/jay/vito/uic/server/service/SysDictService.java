package com.jay.vito.uic.server.service;

import com.jay.vito.storage.service.EntityCRUDService;
import com.jay.vito.uic.server.domain.SysDict;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 用户服务
 */
public interface SysDictService extends EntityCRUDService<SysDict, Long> {

    List<SysDict> findByName(String name);

}
