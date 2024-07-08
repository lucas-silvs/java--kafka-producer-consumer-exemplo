package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import java.util.Map;

public interface KafkaPropertiesConfigurator {

    void configure(Map<String, Object> props);

}
