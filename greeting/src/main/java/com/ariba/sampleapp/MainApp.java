package com.ariba.sampleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Entry class of sampleapp-java
 */
@SpringBootApplication(scanBasePackages = "com.ariba")
public class MainApp {

    /**
     * Entry point of sampleapp-java. Run the spring application
     *
     * @param args
     */
    public static void main(String[] args) {
    
        SpringApplication.run(MainApp.class, args);

    }
}
