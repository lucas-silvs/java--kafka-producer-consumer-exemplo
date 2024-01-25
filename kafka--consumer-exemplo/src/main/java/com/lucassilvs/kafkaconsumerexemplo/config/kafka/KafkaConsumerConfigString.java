package com.lucassilvs.kafkaconsumerexemplo.config.kafka;

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

@Profile("string")
@EnableKafka
@Configuration
public class KafkaConsumerConfigString {


    private ConsumerFactory<String, String> consumerFactory(final KafkaProperties kafkaProperties){
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();

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

}
