package com.vito.uic;

import com.vito.storage.core.MyJpaRepositoryImpl;
import com.vito.storage.core.MyRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories(basePackages = {"com.vito"}, repositoryBaseClass = MyJpaRepositoryImpl.class, repositoryFactoryBeanClass = MyRepositoryFactoryBean.class)
//@MapperScan(basePackages = "com.vito", markerInterface = MybatisMapper.class)  mapper对应的class使用@Mapper注解即可，这是默认方式
@ComponentScan("com.vito")
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

}

