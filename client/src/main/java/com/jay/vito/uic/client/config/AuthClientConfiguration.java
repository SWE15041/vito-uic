package com.jay.vito.uic.client.config;

import com.jay.vito.uic.client.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Order(5)
@Configuration
public class AuthClientConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getUserAuthInterceptor());
    }

    @Bean
    UserAuthInterceptor getUserAuthInterceptor() {
        return new UserAuthInterceptor();
    }

}