package com.lucassilvs.kafkaconsumerexemplo.config.kafka;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("confluent")
@EnableKafka
public class KafkaConsumerConfigConfluent {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.timeout}")
    private Integer timeOut;

    @Value("${spring.kafka.schema-registry.url}")
    private String schemaRegistry;

    @Value("${spring.kafka.properties.basic.auth.credentials.source}")
    private String schemaRegistryCredentialsSource;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.basic.auth.user.info.username}")
    private String schemaRegistryUserinforUsername;

    @Value("${spring.kafka.properties.basic.auth.user.info.password}")
    private String schemaRegistryUserinforPassword;

    @Value("${spring.kafka.properties.sasl.key}")
    private String clusterCredentialsKey;

    @Value("${spring.kafka.properties.sasl.password}")
    private String clusterCredentialsPassword;

    @Value("${spring.application.name}")
    private String clientId;

    private  static  final String SAAS_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";

    @Bean
    public ConsumerFactory<String, UsuarioTesteAvro> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeOut);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

        //Autenticação Cluster Kafka
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(SAAS_JAAS_CONFIG, clusterCredentialsKey, clusterCredentialsPassword));
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name);

        //Utilizando AVRO
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");



        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));
        props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, schemaRegistryCredentialsSource);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,UsuarioTesteAvro>> kafkalisternerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,UsuarioTesteAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
