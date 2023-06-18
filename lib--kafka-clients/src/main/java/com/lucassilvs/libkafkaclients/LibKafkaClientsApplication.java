package com.lucassilvs.libkafkaclients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class LibKafkaClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibKafkaClientsApplication.class, args);
	}

}
