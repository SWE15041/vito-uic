package com.jay.vito.uic.core.config;

import com.jay.vito.uic.web.AppInitListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public ServletListenerRegistrationBean<AppInitListener> getAppInitListener() {
        ServletListenerRegistrationBean<AppInitListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new AppInitListener());
        registrationBean.setOrder(5);
        return registrationBean;
    }

}