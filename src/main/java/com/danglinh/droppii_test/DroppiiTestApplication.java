package com.danglinh.droppii_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
public class DroppiiTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroppiiTestApplication.class, args);
    }

}
