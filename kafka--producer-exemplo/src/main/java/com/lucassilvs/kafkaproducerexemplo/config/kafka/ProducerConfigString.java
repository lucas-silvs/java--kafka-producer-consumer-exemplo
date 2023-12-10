package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Profile("string")
@Configuration
public class ProducerConfigString {


    private Map<String, Object> producerConfigs(KafkaProperties kafkaProperties, String clientId) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }

    private ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, String clientId) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties, clientId));
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(final KafkaProperties kafkaProperties) {
        ProducerFactory<String, String> producerFactory = producerFactory(kafkaProperties,"producer1");

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }


}
