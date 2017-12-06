package com.vito.uic.domain;

import com.vito.storage.domain.MybatisMapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends MybatisMapper {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Long countList(Map<String, Object> params);

    List<String> queryUserResources(Long userId);

}