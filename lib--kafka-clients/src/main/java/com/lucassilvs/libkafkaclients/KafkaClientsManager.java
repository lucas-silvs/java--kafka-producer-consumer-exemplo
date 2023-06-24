package com.lucassilvs.libkafkaclients;


import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
public class KafkaClientsManager  implements SmartInitializingSingleton {

    private final Map<String, KafkaTemplate> listaProducers;
    private final Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers;


    @Autowired
    private ConfigurableApplicationContext applicationContext;
    public KafkaClientsManager(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers,
                               @Qualifier("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers) {
        this.listaProducers = listaProducers;
        this.listaConsumers = listaConsumers;
    }

    @Override
    public void afterSingletonsInstantiated() {
        listaConsumers.forEach((nomeConsumer, consumer) -> {
            applicationContext.getBeanFactory().registerSingleton(nomeConsumer, consumer);
        });
        listaProducers.forEach((nomeProducer, producer) -> {
            applicationContext.getBeanFactory().registerSingleton(nomeProducer, producer);
        });
    }
}