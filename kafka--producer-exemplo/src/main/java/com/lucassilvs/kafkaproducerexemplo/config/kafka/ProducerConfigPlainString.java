package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Profile({"plain & string"})
@Configuration
public class ProducerConfigPlainString {

    @Value("${spring.kafka.producer.properties.plain.username}")
    private String username;

    @Value("${spring.kafka.producer.properties.plain.password}")
    private String password;
    private final String PLAIN_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";


    public ProducerFactory<String, String> producerFactory(final KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(PLAIN_JAAS_CONFIG, username, password));


        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }


}
