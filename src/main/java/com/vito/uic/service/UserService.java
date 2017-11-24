package com.vito.uic.service;

import com.vito.storage.service.EntityCRUDService;
import com.vito.uic.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/11/23 16:26
 * 描述: 用户服务
 */
public interface UserService extends EntityCRUDService<User, Long> {

    User findByLoginName(String loginName);

    List<Map<String, Object>> query(Map<String, Object> params);

}
