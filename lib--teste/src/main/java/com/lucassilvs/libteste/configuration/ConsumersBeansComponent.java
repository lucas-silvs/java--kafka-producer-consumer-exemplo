package com.lucassilvs.libteste.configuration;

import com.lucassilvs.libkafkaclients.KafkaClientsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsumersBeansComponent {

    private final KafkaClientsManager kafkaClientsManager;

    @Autowired
    public ConsumersBeansComponent(KafkaClientsManager kafkaClientsManager) {
        this.kafkaClientsManager = kafkaClientsManager;
    }

    @Bean("containerFactoryConsumer1")
    public ConcurrentKafkaListenerContainerFactory criaBeanConsumer1() {
        return  kafkaClientsManager.buscaConsumer("consumer1");
    }

    @Bean("containerFactoryConsumer2")
    public ConcurrentKafkaListenerContainerFactory criaBeanConsumer2() {
        return  kafkaClientsManager.buscaConsumer("consumer2");
    }




}
