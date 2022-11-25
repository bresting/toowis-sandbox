package com.toowis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * https://jstobigdata.com/spring/custom-events-and-generic-events-in-spring/
 */
@EnableAsync
@SpringBootApplication
public class ToowisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToowisApplication.class, args);
    }
}