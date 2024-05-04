package com.example.logendpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class LogEndpointApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogEndpointApplication.class, args);
    }
}
