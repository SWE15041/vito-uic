package com.jay.vito.uic.core;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import com.jay.vito.storage.domain.MybatisMapper;
import com.jay.vito.website.core.config.EnableVitoWebServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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

    /*@Configuration("userConfig")
    @ConditionalOnProperty(name = "uic.user.enabled", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackageClasses = {SysUserService.class, SysUserController.class, SysUserRoleService.class, LoginController.class})
    public class UserAutoConfiguration {

        @Bean
        public SysUserService getUserService() {
            return new SysUserServiceImpl();
        }

    }

    @Configuration("roleConfig")
    @ConditionalOnProperty(name = "uic.role.enabled", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackageClasses = {SysRoleService.class, SysRoleResourceService.class, SysRoleController.class})
    public class RoleAutoConfiguration {
    }

    @Configuration("resourceConfig")
    @ConditionalOnProperty(name = "uic.resource.enabled", havingValue = "true", matchIfMissing = true)
    @ComponentScan(basePackageClasses = {SysResourceService.class, SysResourceController.class})
    public class ResourceAutoConfiguration {
    }*/

}