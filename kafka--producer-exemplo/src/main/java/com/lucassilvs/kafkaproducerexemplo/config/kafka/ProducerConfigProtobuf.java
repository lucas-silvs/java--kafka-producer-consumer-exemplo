package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Profile("protobuf")
@Configuration
public class ProducerConfigProtobuf {

    @Bean
    public ProducerFactory<String, SpecificRecord> producerFactory(final KafkaProperties kafkaProperties) {
        Map<String, Object> props =  kafkaProperties.buildProducerProperties();


        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, SpecificRecord> kafkaTemplate(ProducerFactory<String, SpecificRecord> producerFactory){

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }


}
