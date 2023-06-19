package com.lucassilvs.libteste.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsumersBeansComponent {



    @Bean("containerFactoryConsumer1")
    public ConcurrentKafkaListenerContainerFactory<String, String> criaBeanConsumer1() {
        return KafkaClientsManager.buscaConsumer("consumer1");
    }

    @Bean("containerFactoryConsumer2")
    public ConcurrentKafkaListenerContainerFactory<String, String> criaBeanConsumer2() {
        return KafkaClientsManager.buscaConsumer("consumer2");
    }




}
