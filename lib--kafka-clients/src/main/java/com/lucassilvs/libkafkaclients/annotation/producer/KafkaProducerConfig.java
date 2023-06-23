package com.lucassilvs.libkafkaclients.annotation.producer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


    @Bean
    public KafkaProducerConfigBeanPostProcessor kafkaClientBeanPostProcessor(@Qualifier("listProducers") Map<String, KafkaTemplate> listaProducers,
                                                                             @Qualifier("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers) {
        return new KafkaProducerConfigBeanPostProcessor(listaProducers, listaConsumers);
    }

    private static class KafkaProducerConfigBeanPostProcessor implements BeanPostProcessor {

        private final Map<String, KafkaTemplate> listaProducers;
        private final Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers;

        public KafkaProducerConfigBeanPostProcessor( Map<String, KafkaTemplate> listaProducers,
                                                     Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listaConsumers) {
            this.listaProducers = listaProducers;
            this.listaConsumers = listaConsumers;
        }


        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            Class<?> beanClass = bean.getClass();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(KafkaProducer.class)) {
                    KafkaProducer kafkaClientAnnotation = field.getAnnotation(KafkaProducer.class);
                    String kafkaClientName = kafkaClientAnnotation.value();
                    KafkaTemplate kafkaClient = getKafkaProducerByName(kafkaClientName);
                    field.setAccessible(true);
                    try {
                        field.set(bean, kafkaClient);
                    } catch (IllegalAccessException e) {
                        throw new BeansException("Error setting Kafka client field: " + field.getName(), e) {
                        };
                    }
                }
            }
            return bean;
        }

        // Implemente a lógica para obter o Kafka Producer/Consumer pelo nome fornecido
        private KafkaTemplate getKafkaProducerByName(String name) {
            // Retorne o Kafka Producer/Consumer com base no nome
            return listaProducers.get(name);
        }
    }
}

