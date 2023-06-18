package com.lucassilvs.libteste.configuration;


import com.lucassilvs.libkafkaclients.clients.configuration.ProducerListComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerListConfiguration {


    @Bean
    public ProducerListComponent producerListComponent() {
        return new ProducerListComponent();
    }
}
