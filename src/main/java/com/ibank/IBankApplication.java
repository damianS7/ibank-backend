package com.ibank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IBankApplication {

    public static void main(String[] args) {
        System.setProperty("spring.main.lazy-initialization", "false");
        SpringApplication.run(IBankApplication.class, args);
    }
}
