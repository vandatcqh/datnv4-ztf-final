package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class TokenServiceApp {
    private static final Logger log = LoggerFactory.getLogger(TokenServiceApp.class);
    public static void main(String[] args) {

        SpringApplication.run(TokenServiceApp.class, args);
        log.info("✅ Test log via Spring Boot context");
        log.info("✅ Test log via Spring Boot context");
        log.info("✅ Test log via Spring Boot context");
        log.info("✅ Test log via Spring Boot context");
        log.info("✅ Test log via Spring Boot context");
        log.info("✅ Test log via Spring Boot context");

    }
}
