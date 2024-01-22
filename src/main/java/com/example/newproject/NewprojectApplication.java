package com.example.newproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewprojectApplication.class, args);
    }

}
