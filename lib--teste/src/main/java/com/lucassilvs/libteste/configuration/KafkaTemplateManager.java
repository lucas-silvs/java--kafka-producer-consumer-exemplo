package com.lucassilvs.libteste.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaTemplateManager {


    private static Map<String, KafkaTemplate> listaProducers;

    @Autowired
    public KafkaTemplateManager(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers) {
        KafkaTemplateManager.listaProducers = listaProducers;
    }

    public static KafkaTemplate getKafkaTemplate(String nomeProducer) {
        return listaProducers.get(nomeProducer);
    }



}
