package com.jay.vito.uic.core.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jay.vito.uic.web.AppInitListener;
import com.jay.vito.website.core.config.MyFastJsonHttpMessageConverter;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public ServletListenerRegistrationBean<AppInitListener> getAppInitListener() {
        ServletListenerRegistrationBean<AppInitListener> registrationBean = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(new AppInitListener());
        registrationBean.setOrder(5);
        return registrationBean;
    }

/*    @Bean
    public FilterRegistrationBean getAuthFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthServerFilter());
        registrationBean.setOrder(5);
        List<String> match = new ArrayList<>();
        match.add("/*");
        registrationBean.setUrlPatterns(match);
        return registrationBean;
    }*/

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        return new MyFastJsonHttpMessageConverter();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .allowedOrigins("*");
            }

            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(fastJsonHttpMessageConverter());
                super.configureMessageConverters(converters);
            }
        };
    }

}