package com.jay.vito.uic.client.config;

import com.jay.vito.uic.client.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Order(5)
@Configuration("authClientConfig")
public class AutoConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserAuthRestInterceptor());
        super.addInterceptors(registry);
    }

    @Bean
    UserAuthInterceptor getUserAuthRestInterceptor() {
        return new UserAuthInterceptor();
    }

}