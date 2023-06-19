package com.lucassilvs.libteste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication (scanBasePackages = "com.lucassilvs.libteste")
@EnableConfigurationProperties
public class LibTesteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibTesteApplication.class, args);
    }

}
