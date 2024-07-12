package com.lucassilvs.kafkaconsumerexemplo.config.kafka.configurator;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Profile("confluent")
@Configuration
public class ConfiguratorConfluent implements KafkaPropertiesConfigurator {

    @Value("${spring.kafka.properties.basic.auth.credentials.source}")
    private String schemaRegistryCredentialsSource;

    @Value("${spring.kafka.properties.basic.auth.user.info.username}")
    private String schemaRegistryUserinforUsername;

    @Value("${spring.kafka.properties.basic.auth.user.info.password}")
    private String schemaRegistryUserinforPassword;

    @Value("${spring.kafka.properties.sasl.key}")
    private String clusterCredentialsKey;

    @Value("${spring.kafka.properties.sasl.password}")
    private String clusterCredentialsPassword;

    private  static  final String SAAS_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";


    @Override
    public void configure(Map<String, Object> props) {

        //Autenticação Cluster Kafka
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(SAAS_JAAS_CONFIG, clusterCredentialsKey, clusterCredentialsPassword));

        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));
        props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, schemaRegistryCredentialsSource);

    }
}
