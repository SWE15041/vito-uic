package com.jay.vito.uic.core;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.storage.domain.MybatisMapper;
import com.jay.vito.uic.service.SysResourceService;
import com.jay.vito.uic.service.SysRoleService;
import com.jay.vito.uic.service.SysUserService;
import com.jay.vito.uic.service.impl.SysResourceServiceImpl;
import com.jay.vito.uic.service.impl.SysRoleServiceImpl;
import com.jay.vito.uic.service.impl.SysUserServiceImpl;
import com.jay.vito.website.core.config.EnableVitoWebServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration("authServerConfig")
@EnableJpaRepositories(basePackages = {"com.jay.vito.uic.domain"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@EntityScan("com.jay.vito.uic.domain")
@MapperScan(basePackages = "com.jay.vito.uic.domain", markerInterface = MybatisMapper.class)
@ComponentScan(basePackages = {"com.jay.vito.uic.service"})
@EnableVitoWebServer
public class AutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public SysUserService getUserService() {
        return new SysUserServiceImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public SysRoleService getRoleService() {
        return new SysRoleServiceImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public SysResourceService getResourceService() {
        return new SysResourceServiceImpl();
    }

    @Configuration
    @ConditionalOnProperty(name = "uic.controller.enable", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackages = {"com.jay.vito.uic.web"})
    public static class ControllerConfiguration {

    }

}