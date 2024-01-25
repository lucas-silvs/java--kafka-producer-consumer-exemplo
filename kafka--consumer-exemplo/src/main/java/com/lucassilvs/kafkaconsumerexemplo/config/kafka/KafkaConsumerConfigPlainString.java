package com.lucassilvs.kafkaconsumerexemplo.config.kafka;

import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Duration;
import java.util.Map;

@Profile("string & plain")
@EnableKafka
@Configuration
public class KafkaConsumerConfigPlainString {

    @Value("${}")
    private String username;

    @Value("${}")
    private String password;

    private static final String PLAIN_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";

    private ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties){
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(PLAIN_JAAS_CONFIG, username, password));

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> consumer1(ConsumerFactory<String, String> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));

        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> consumer2(ConsumerFactory<String, String> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));

        return factory;
    }

}
