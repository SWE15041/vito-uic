package com.jay.vito.uic.domain;

import com.jay.vito.storage.domain.MybatisMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper extends MybatisMapper {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Long countList(Map<String, Object> params);

    List<String> queryUserResources(Long userId);

}