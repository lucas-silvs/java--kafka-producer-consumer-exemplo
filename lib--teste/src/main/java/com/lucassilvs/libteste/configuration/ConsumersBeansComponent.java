package com.lucassilvs.libteste.configuration;

import com.lucassilvs.libkafkaclients.KafkaClientsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumersBeansComponent {

    private final KafkaClientsManager kafkaClientsManager;

    @Autowired
    public ConsumersBeansComponent(KafkaClientsManager kafkaClientsManager) {
        this.kafkaClientsManager = kafkaClientsManager;
    }
}
