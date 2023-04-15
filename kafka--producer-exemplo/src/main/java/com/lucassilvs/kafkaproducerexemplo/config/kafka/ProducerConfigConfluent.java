package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Profile("confluent")
@Configuration
public class ProducerConfigConfluent {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.ack}")
    private String acknowledgement;

    @Value("${spring.kafka.producer.number-of-tries}")
    private Integer numberOfTries;

    @Value("${spring.kafka.producer.imdepotence}")
    private boolean imdepotence;

    @Value("${spring.kafka.producer.timeout}")
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

    private  static  final String SAAS_JAAS_CONFIG = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, acknowledgement);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, numberOfTries);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeOut);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, imdepotence);

        //Autenticação Cluster Kafka
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(SAAS_JAAS_CONFIG, clusterCredentialsKey, clusterCredentialsPassword));
        props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.name);

        //Utilizando AVRO
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        props.put(KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG, true);

        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));
        props.put(AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE, schemaRegistryCredentialsSource);

        return props;
    }

    @Bean
    public ProducerFactory<String, UsuarioTesteAvro> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


}
