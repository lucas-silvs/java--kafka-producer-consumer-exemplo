package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Profile("string")
@Configuration
public class ProducerConfigString {

    private final String PLAIN_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
    private Map<String, Object> producerConfigs(KafkaProperties kafkaProperties, String username, String password, String clientId) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(PLAIN_JAAS_CONFIG, username, password));

        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }

    private ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, String username, String password, String clientId) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties, username, password, clientId));
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(final KafkaProperties kafkaProperties) {
        ProducerFactory<String, String> producerFactory = producerFactory(kafkaProperties, "producer", "producer-secret", "producer1");

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateTopico2(final KafkaProperties kafkaProperties) {
        ProducerFactory<String, String> producerFactory = producerFactory(kafkaProperties, "producer2", "teste", "producer2");

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }


}
