package com.jay.vito.uic.domain;

import com.jay.vito.storage.domain.MybatisMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper extends MybatisMapper {

    List<Map<String, Object>> selectList(Map<String, Object> params);

    Long countList(Map<String, Object> params);

    List<String> queryUserResources(@Param("userId") Long userId, @Param("groupId") Long groupId);

    List<String> queryUserRoles(@Param("userId") Long userId);


}