package com.jay.vito.uic.core;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.storage.domain.MybatisMapper;
import com.jay.vito.uic.service.*;
import com.jay.vito.uic.web.controller.*;
import com.jay.vito.website.core.config.EnableVitoWebServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration("authServerConfig")
@EnableJpaRepositories(basePackages = {"com.jay.vito.uic.domain"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@EntityScan("com.jay.vito.uic.domain")
@MapperScan(basePackages = "com.jay.vito.uic.domain", markerInterface = MybatisMapper.class)
@EnableVitoWebServer
public class AutoConfiguration {

    @ConditionalOnProperty(name = "uic.userBean.enabled", matchIfMissing = true)
    @Configuration("userBeanConfig")
    @ComponentScan(basePackageClasses = {SysUserService.class, SysUserController.class, LoginController.class})
    public static class UserConfiguration {
    }

    @ConditionalOnProperty(name = "uic.roleBean.enabled", matchIfMissing = true)
    @Configuration("roleBeanConfig")
    @ComponentScan(basePackageClasses = {SysRoleService.class, SysUserRoleService.class, SysRoleController.class})
    public static class RoleConfiguration {
    }

    @ConditionalOnProperty(name = "uic.resourceBean.enabled", matchIfMissing = true)
    @Configuration("resourceBeanConfig")
    @ComponentScan(basePackageClasses = {SysResourceService.class, SysRoleResourceService.class, SysResourceController.class})
    public static class ResourceConfiguration {
    }

    @ConditionalOnProperty(name = "uic.dictBean.enabled", matchIfMissing = true)
    @Configuration("dictBeanConfig")
    @ComponentScan(basePackageClasses = {SysDictService.class, SysDictController.class})
    public static class DictConfiguration {
    }

}