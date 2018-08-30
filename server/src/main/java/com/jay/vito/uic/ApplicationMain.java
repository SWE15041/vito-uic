package com.jay.vito.uic;

import com.jay.vito.storage.core.MyJpaRepositoryImpl;
import com.jay.vito.storage.core.MyRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableEurekaClient
@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.jay.vito"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
//@MapperScan(basePackages = "com.vito", markerInterface = MybatisMapper.class)  mapper对应的class使用@Mapper注解即可，这是默认方式
@ComponentScan("com.jay.vito")
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

}

