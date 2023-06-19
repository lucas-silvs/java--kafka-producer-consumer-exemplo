package com.lucassilvs.libkafkaclients;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaClientsManager {

    private final Map<String, KafkaTemplate> listaProducers;
    private final Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers;

    public KafkaClientsManager(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers,
                               @Qualifier("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers) {
        this.listaProducers = listaProducers;
        this.listaConsumers = listaConsumers;
    }

    public ConcurrentKafkaListenerContainerFactory<String, Object> buscaConsumer(String nomeConsumer) {
        return listaConsumers.get(nomeConsumer);
    }

    public KafkaTemplate buscaProducer(String nomeProducer) {
        return listaProducers.get(nomeProducer);
    }
}