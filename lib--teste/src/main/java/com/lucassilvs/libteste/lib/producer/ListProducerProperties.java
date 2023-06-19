package com.lucassilvs.libteste.lib.producer;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "kafka")
@Order(0)
public class ListProducerProperties {

    private Map<String,ProducerCommonProperties> producers;
}