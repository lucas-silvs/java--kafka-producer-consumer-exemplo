package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.List;
import java.util.Map;

@Configuration
public class ProducerConfig {

    private final List<KafkaPropertiesConfigurator> configurators;

    @Autowired
    public ProducerConfig(List<KafkaPropertiesConfigurator> configurators) {
        this.configurators = configurators;
    }


    @Bean
    public ProducerFactory<String, Object> producerFactory(final KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildProducerProperties(null);

        props.put(org.apache.kafka.clients.producer.ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());

        for (KafkaPropertiesConfigurator configurator : configurators) {
            configurator.configure(props);
        }

        return new DefaultKafkaProducerFactory<>(props);
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        // inicializa o producer no start da aplicação
        producerFactory.createProducer();

        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        // Habilita Tracing nas mensagens enviadas adicionando no header o traceparent
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }
    @Bean
    public KafkaAdmin adminClient(final KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildAdminProperties(null);
        for (KafkaPropertiesConfigurator configurator : configurators) {
            configurator.configure(props);
        }

        return  new KafkaAdmin(props);

    }

}
