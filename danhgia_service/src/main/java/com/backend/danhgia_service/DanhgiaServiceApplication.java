package com.backend.danhgia_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.backend.danhgia_service","com.backend.commonservice"})
public class DanhgiaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DanhgiaServiceApplication.class, args);
    }

}
