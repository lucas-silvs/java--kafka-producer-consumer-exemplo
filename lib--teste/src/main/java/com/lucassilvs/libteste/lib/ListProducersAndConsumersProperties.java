package com.lucassilvs.libteste.lib;


import com.lucassilvs.libteste.lib.consumer.ConsumerCommonProperties;
import com.lucassilvs.libteste.lib.producer.ProducerCommonProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "kafka")
public class ListProducersAndConsumersProperties {

    private Map<String, ProducerCommonProperties> producers;

    private Map<String, ConsumerCommonProperties> consumers;
}
