package com.lucassilvs.libkafkaclients.configuration;

import com.lucassilvs.libkafkaclients.properties.ListProducersAndConsumersProperties;
import com.lucassilvs.libkafkaclients.properties.authentication.KafkaAuthProperties;
import com.lucassilvs.libkafkaclients.properties.producer.ProducerCommonProperties;
import com.lucassilvs.libkafkaclients.properties.schema_registry.SchemaRegistryConfiguration;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import jakarta.annotation.Nonnull;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerListComponent {

    @Autowired
    private ListProducersAndConsumersProperties listProducerProperties;

    public Map<String, Object> producerConfigs(ProducerCommonProperties producerProperties) {
        Map<String, Object> props = new HashMap<>();

        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getBootstrapServers());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG, producerProperties.getClientId());

        KafkaAuthProperties kafkaAuthProperties = producerProperties.getAuth();
        SchemaRegistryConfiguration registryConfiguration = producerProperties.getSchemaRegistry();

        props = processFields(props, producerProperties);

        if( kafkaAuthProperties != null ) {

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

    private Map<String, Object> processFields(Map<String, Object> props, ProducerCommonProperties producerProperties) {
            Field[] fields = ProducerCommonProperties.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(producerProperties);
                    if (value != null || field.isAnnotationPresent(Nonnull.class)) {
                        String configKey = convertFieldNameToConfigKey(field.getName());
                        props.put(configKey, value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                    // Tratar a exceção adequadamente
                }
            }
        return props;
    }

    private String convertFieldNameToConfigKey(String fieldName) {
        String[] parts = fieldName.split("(?<=.)(?=\\p{Upper})");
        return String.join(".", parts).toLowerCase();
    }

    public ProducerFactory producerFactory(ProducerCommonProperties producerCommonProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(producerCommonProperties));
    }

    @Bean("listProducers")
    public Map<String, KafkaTemplate> listaKafkaTemplate() {
        Map<String, KafkaTemplate> kafkaTemplates = new HashMap<>();
        listProducerProperties.getProducers().forEach((name, producerPropertie) -> {
            KafkaTemplate<String, KafkaTemplate> kafkaTemplate = new KafkaTemplate<>(producerFactory(producerPropertie));
            kafkaTemplates.put(name, kafkaTemplate);
        });
        return kafkaTemplates;
    }
}

