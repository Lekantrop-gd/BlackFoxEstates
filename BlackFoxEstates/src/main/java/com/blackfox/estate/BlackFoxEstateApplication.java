package com.blackfox.estate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlackFoxEstateApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlackFoxEstateApplication.class, args);
    }
}
