package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Profile("avro")
@Configuration
public class ProducerConfigAvro {

//    @Bean
//    public Map<String, Object> producerConfigs( KafkaProperties kafkaProperties) {
//        Map<String, Object> props = kafkaProperties.buildProducerProperties();
//        return props;
//    }

    @Bean
    public ProducerFactory<String, Object> producerFactory(final KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }

}
