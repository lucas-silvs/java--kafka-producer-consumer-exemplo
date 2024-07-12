package com.lucassilvs.kafkaconsumerexemplo.config.kafka.configurator;

import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Profile({"plain"})
@Configuration
public class ConfiguratorPlain implements KafkaPropertiesConfigurator {

    @Value("${spring.kafka.properties.plain.username}")
    private String username;

    @Value("${spring.kafka.properties.plain.password}")
    private String password;


    @Override
    public void configure(Map<String, Object> props) {
        String plainAuth = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(plainAuth, username, password));
    }
}
