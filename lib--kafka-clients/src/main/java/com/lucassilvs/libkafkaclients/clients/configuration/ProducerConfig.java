package com.lucassilvs.libkafkaclients.clients.configuration;


import com.lucassilvs.libkafkaclients.clients.authentication.KafkaAuthProperties;
import com.lucassilvs.libkafkaclients.clients.producer.ProducerCommonProperties;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ProducerConfig {

    @Autowired
    private  List<ProducerCommonProperties> ProducerProperties;

    public static List<KafkaTemplate> listProducers;

    public Map<String, Object> producerConfigs(ProducerCommonProperties producerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getBootstrapServers());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, producerProperties.getAcks());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, producerProperties.getRetries());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProperties.getRequestTimeoutMs());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, producerProperties.getEnableIdempotence());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG, producerProperties.getClientId());

        KafkaAuthProperties kafkaAuthProperties = producerProperties.getAuth();
        props.put(SaslConfigs.SASL_MECHANISM, kafkaAuthProperties.getSaslMechanism());
        props.put(SaslConfigs.SASL_LOGIN_CALLBACK_HANDLER_CLASS, kafkaAuthProperties.getSaslLoginCallbackHandlerClass());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, producerProperties.getSecurityProtocol());

        switch (kafkaAuthProperties.getSaslMechanism()){
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

        //Utilizando AVRO
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerProperties.getKeySerializer());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,producerProperties.getValueSerializer());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, producerProperties.getSchemaRegistry().getUrl());

        if (producerProperties.getSchemaRegistry().isBasicAuth()) {
            props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s", producerProperties.getSchemaRegistry().getUsername(), producerProperties.getSchemaRegistry().getPassword()));
            props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO");
        }
        props.put(KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG, true);

        return props;
    }

    public ProducerFactory producerFactory(ProducerCommonProperties producerCommonProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(producerCommonProperties));
    }

    @Bean
    public List<KafkaTemplate> kafkaTemplate() {

        for (ProducerCommonProperties producerProperties : ProducerProperties) {
            Map<String, Object> props = producerConfigs(producerProperties);
            KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory(producerProperties));
            listProducers.add(kafkaTemplate);
        }
        return listProducers;
    }
}

