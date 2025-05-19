package com.backend.aichatbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableDiscoveryClient
public class AiChatboxApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiChatboxApplication.class, args);
    }

}
