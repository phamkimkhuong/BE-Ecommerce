package com.backend.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.backend.notificationservice", "com.backend.commonservice"})
//@ComponentScan({"com.backend.notificationservice", "com.backend.commonservice"})
class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
