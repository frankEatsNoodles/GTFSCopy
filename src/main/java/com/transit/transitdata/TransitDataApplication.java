package com.transit.transitdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransitDataApplication {

    public static void main(String[] args) {

        SpringApplication.run(TransitDataApplication.class, args);

    }

}
