package com.vito.uic.web.config;

import com.vito.uic.web.AppInitListener;
import com.vito.uic.web.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public ServletListenerRegistrationBean<AppInitListener> getAppInitListener() {
        ServletListenerRegistrationBean<AppInitListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new AppInitListener());
        registrationBean.setOrder(5);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean getAuthFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthFilter());
        registrationBean.setOrder(5);
        return registrationBean;
    }

}