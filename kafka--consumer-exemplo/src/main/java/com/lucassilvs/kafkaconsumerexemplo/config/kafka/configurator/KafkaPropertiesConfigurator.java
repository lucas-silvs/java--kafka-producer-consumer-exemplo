package com.lucassilvs.kafkaconsumerexemplo.config.kafka.configurator;

import java.util.Map;

public interface KafkaPropertiesConfigurator {

    void configure(Map<String, Object> props);

}
