package com.jay.vito.uic.server;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.storage.domain.MybatisMapper;
import com.jay.vito.uic.server.service.SysResourceService;
import com.jay.vito.uic.server.service.SysRoleService;
import com.jay.vito.uic.server.service.SysUserService;
import com.jay.vito.uic.server.service.impl.SysResourceServiceImpl;
import com.jay.vito.uic.server.service.impl.SysRoleServiceImpl;
import com.jay.vito.uic.server.service.impl.SysUserServiceImpl;
import com.jay.vito.website.EnableVitoWebServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration("vitoAuthServerConfiguration")
@EnableJpaRepositories(repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@EntityScan
@MapperScan(basePackages = {"com.jay.vito.uic.server.repository"}, markerInterface = MybatisMapper.class)
@ComponentScan(basePackages = {"com.jay.vito.uic.server.service"})
@EnableVitoWebServer
public class AuthServerConfiguration {

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
	@ComponentScan(basePackages = {"com.jay.vito.uic.server.web"})
	public static class ControllerConfiguration {

	}

}