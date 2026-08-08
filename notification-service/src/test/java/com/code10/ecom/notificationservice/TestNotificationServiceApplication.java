package com.code10.ecom.notificationservice;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NotificationServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
