package com.lucassilvs.libteste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.lucassilvs")
@EnableConfigurationProperties
public class LibTesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibTesteApplication.class, args);
    }

}
