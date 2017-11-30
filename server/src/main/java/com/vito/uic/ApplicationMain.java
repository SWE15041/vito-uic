package com.vito.uic;

import com.vito.storage.core.MyJpaRepositoryImpl;
import com.vito.storage.core.MyRepositoryFactoryBean;
import com.vito.storage.domain.MybatisMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.vito"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
@SpringBootApplication
@MapperScan(basePackages = "com.vito", markerInterface = MybatisMapper.class)
@ComponentScan("com.vito")
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

}
