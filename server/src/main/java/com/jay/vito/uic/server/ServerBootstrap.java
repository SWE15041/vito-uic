package com.jay.vito.uic.server;

import com.jay.vito.uic.client.config.EnableAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableAuthServer
@EnableAuthClient
public class ServerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ServerBootstrap.class, args);
    }

}
