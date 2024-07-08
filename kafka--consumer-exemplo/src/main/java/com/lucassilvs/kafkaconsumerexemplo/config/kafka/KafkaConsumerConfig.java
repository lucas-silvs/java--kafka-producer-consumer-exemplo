package com.lucassilvs.kafkaconsumerexemplo.config.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("avro")
public class KafkaConsumerConfig {

    private final List<KafkaPropertiesConfigurator> configurators;

    @Autowired
    public KafkaConsumerConfig(List<KafkaPropertiesConfigurator> configurators) {
        this.configurators = configurators;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(final KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildConsumerProperties(null);


        for (KafkaPropertiesConfigurator configurator: configurators){
            configurator.configure(props);
        }

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,Object>> kafkalisternerContainerFactory(ConsumerFactory<String, Object> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.getContainerProperties().setObservationEnabled(true);

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));
        factory.setConsumerFactory(consumerFactory);
        return factory;
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
