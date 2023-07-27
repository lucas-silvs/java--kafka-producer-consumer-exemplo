package com.lucassilvs.libkafkaclients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
public class KafkaClientsManager {

    private final Map<String, KafkaTemplate> listaProducers;
    private final Map<String, ConcurrentKafkaListenerContainerFactory> listaConsumers;
    private final ConfigurableApplicationContext applicationContext;


    @Autowired
    public KafkaClientsManager(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers,
                               @Qualifier("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory> listaConsumers,
                               ConfigurableApplicationContext applicationContext) {
        this.listaProducers = listaProducers;
        this.listaConsumers = listaConsumers;
        this.applicationContext = applicationContext;

        listaConsumers.forEach((nomeConsumer, consumer) -> {
            this.applicationContext.getBeanFactory().registerSingleton(nomeConsumer, consumer);
        });
        listaProducers.forEach((nomeProducer, producer) -> {
            this.applicationContext.getBeanFactory().registerSingleton(nomeProducer, producer);
        });
    }
}