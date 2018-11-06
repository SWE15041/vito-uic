package com.jay.vito.uic;

import com.jay.vito.uic.client.config.EnableAuthClient;
import com.jay.vito.uic.core.EnableAuthServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
//@MapperScan(basePackages = "com.vito", markerInterface = MybatisMapper.class)  mapper对应的class使用@Mapper注解即可，这是默认方式
@EnableAuthServer
@EnableAuthClient
public class ApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

}

