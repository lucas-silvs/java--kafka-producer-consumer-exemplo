package com.lucassilvs.libkafkaclients.clients.producer;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "kafka.producers")
@Configuration
public class ListProducerProperties {

    private Map<String, ProducerCommonProperties> producers;

    public Map<String, ProducerCommonProperties> getProducers() {
        return producers;
    }
}
