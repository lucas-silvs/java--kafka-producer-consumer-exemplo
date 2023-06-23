//package com.lucassilvs.libkafkaclients.annotation.consumer;
//
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//
//import java.util.Map;
//
//@Configuration
//public class KafkaListenerCustomConfig {
//
//    private Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listConsumers;
//
//    public KafkaListenerCustomConfig(@Qualifier("listConsumers") Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listConsumers) {
//        this.listConsumers = listConsumers;
//    }
//
//
//    @Bean
//    public BeanFactoryPostProcessor dynamicConsumerBeanFactoryPostProcessor() {
//        return new DynamicConsumerBeanFactoryPostProcessor(this.listConsumers);
//    }
//
//    private static class DynamicConsumerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
//
//        private Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listConsumers;
//
//        public DynamicConsumerBeanFactoryPostProcessor(Map<String, ConcurrentKafkaListenerContainerFactory<String, Object>> listConsumers) {
//            this.listConsumers = listConsumers;
//        }
//        @Override
//        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//            for (Map.Entry<String, ConcurrentKafkaListenerContainerFactory<String, Object>> entry : listConsumers.entrySet()) {
//                String beanName = entry.getKey();
//                ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory = entry.getValue();
//                beanFactory.registerSingleton(beanName, containerFactory);
//            }
//
//        }
//    }
//}
