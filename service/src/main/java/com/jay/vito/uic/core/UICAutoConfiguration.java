package com.jay.vito.uic.core;

import com.jay.vito.uic.service.SysUserService;
import com.jay.vito.uic.service.impl.SysUserServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

//@Configuration
//@ConditionalOnProperty(prefix = "uic", name = "enabled")
public class UICAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SysUserService initUserService() {
        return new SysUserServiceImpl();
    }

}
