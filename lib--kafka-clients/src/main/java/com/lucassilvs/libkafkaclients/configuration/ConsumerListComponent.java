package com.lucassilvs.libkafkaclients.configuration;


import com.lucassilvs.libkafkaclients.properties.ListProducersAndConsumersProperties;
import com.lucassilvs.libkafkaclients.properties.authentication.KafkaAuthProperties;
import com.lucassilvs.libkafkaclients.properties.consumer.ConsumerCommonProperties;
import com.lucassilvs.libkafkaclients.properties.schema_registry.SchemaRegistryConfiguration;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerListComponent {

    @Autowired
    private ListProducersAndConsumersProperties producersAndConsumersProperties;

    public Map<String, Object> consumerConfigs(ConsumerCommonProperties consumerCommonProperties) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerCommonProperties.getBootstrapServers());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerCommonProperties.getClientId());

        Map<String, Object> propertieMap = processFields(consumerCommonProperties);

        props.putAll(propertieMap);

        KafkaAuthProperties kafkaAuthProperties = consumerCommonProperties.getAuth();
        SchemaRegistryConfiguration registryConfiguration = consumerCommonProperties.getSchemaRegistry();


        if (kafkaAuthProperties != null) {

            switch (kafkaAuthProperties.getSaslMechanism()) {
                case "OAUTHBEARER":
                    String saslJaasConfig = kafkaAuthProperties.getSaslJaasConfig();

                    if (kafkaAuthProperties.getExtensions() != null) {
                        String extension = "";

                        for (Map.Entry<String, String> entry : kafkaAuthProperties.getExtensions().entrySet()) {
                            extension += String.format(" extension_%s=\"%s\"", entry.getKey(), entry.getValue());
                        }
                        extension += ";";
                        saslJaasConfig = saslJaasConfig.replace(";", extension);
                    }
                    props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(saslJaasConfig, kafkaAuthProperties.getAuthClientId(), kafkaAuthProperties.getAuthClientSecret(), kafkaAuthProperties.getScope()));
                    props.put(SaslConfigs.SASL_OAUTHBEARER_TOKEN_ENDPOINT_URL, kafkaAuthProperties.getSaslTokenEndpointUrl());
                    break;

                case "PLAIN", "SCRAM-SHA-256", "SCRAM-SHA-512":
                    props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(kafkaAuthProperties.getSaslJaasConfig(), kafkaAuthProperties.getUsername(), kafkaAuthProperties.getPassword()));
                    break;
            }
        }

        // configurações do schema registry
        if (registryConfiguration != null) {
            props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryConfiguration.getUrl());
            if (registryConfiguration.getUsername() != null && registryConfiguration.getPassword() != null) {
                props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s", registryConfiguration.getUsername(), registryConfiguration.getPassword()));
                props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
            }
        }

        return props;
    }

    private Map<String, Object> processFields(ConsumerCommonProperties kafkaProperties) {
        Field[] fields = ConsumerCommonProperties.class.getDeclaredFields();
        Map<String,Object> propstemp = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(kafkaProperties);
                if (value != null || field.isAnnotationPresent(Nonnull.class)) {
                    String configKey = convertFieldNameToConfigKey(field.getName());
                    propstemp.put(configKey, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ocorreu um erro ao processar as configurações do Kafka ao criar os consumers", e);
            }
        }
        return propstemp;
    }

    private String convertFieldNameToConfigKey(String fieldName) {
        String[] parts = fieldName.split("(?<=.)(?=\\p{Upper})");
        return String.join(".", parts).toLowerCase();
    }

    public ConsumerFactory consumerFactory(ConsumerCommonProperties consumerCommonProperties) {
        return new DefaultKafkaConsumerFactory(consumerConfigs(consumerCommonProperties));
    }

    @Bean("listConsumers")
    public Map<String, ConcurrentKafkaListenerContainerFactory> listaKafkaTemplate() {
        Map<String, ConcurrentKafkaListenerContainerFactory> map = new HashMap<>();
        producersAndConsumersProperties.getConsumers().forEach((name, consumerProperties) -> {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory(consumerProperties));
            map.put(name, factory);
        });
        return map;
    }
}
