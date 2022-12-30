package com.lucassilvs.kafkaconsumerexemplo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class KafkaConsumerExemploApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerExemploApplication.class, args);
	}

}
