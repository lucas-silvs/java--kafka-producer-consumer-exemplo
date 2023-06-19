package com.lucassilvs.libteste.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaClientsManager {


    private static Map<String, KafkaTemplate> listaProducers;

    private static Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers;

    @Autowired
    public KafkaClientsManager(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers, @Qualifier ("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers) {
        KafkaClientsManager.listaProducers = listaProducers;
        KafkaClientsManager.listaConsumers = listaConsumers;
    }

    public static ConcurrentKafkaListenerContainerFactory buscaConsumer(String nomeConsumer) {
        return listaConsumers.get(nomeConsumer);
    }


    public static KafkaTemplate buscaProducer(String nomeProducer) {
        return listaProducers.get(nomeProducer);
    }



}
