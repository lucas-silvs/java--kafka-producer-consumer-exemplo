package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler;
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

@Profile("oidc")
@Configuration
public class ProducerConfigConfluentOIDCProviderAvro {

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

    @Value("${spring.kafka.properties.basic.auth.user.info.username}")
    private String schemaRegistryUserinforUsername;

    @Value("${spring.kafka.properties.basic.auth.user.info.password}")
    private String schemaRegistryUserinforPassword;

    @Value("${spring.kafka.properties.basic.auth.credentials.source}")
    private String schemaRegistryCredentialsSource;

    @Value("${security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${extension.cluster}")
    private String clusterId;

    @Value("${extension.identity-pool}")
    private String identityPool;

    @Value("${spring.kafka.properties.sasl.clientId}")
    private String  clientId;

    @Value("${spring.kafka.properties.sasl.clientSecret}")
    private String clientSecret;

    @Value("${spring.kafka.properties.sasl.oauthbearer.token.endpoint.url}")
    private String tokenEndpoint;

    @Value("${spring.kafka.properties.sasl.scope}")
    private String scope;

    private  static  final String SAAS_JAAS_CONFIG = "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required " +
            "clientId=\"%s\" " +
            "clientSecret=\"%s\" " +
            "scope=\"%s\" " +
            "extension_logicalCluster=\"%s\" " +
            "extension_identityPoolId=\"%s\";";

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, acknowledgement);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, numberOfTries);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, timeOut);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, imdepotence);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-teste");

        props.put("sasl.login.callback.handler.class", OAuthBearerLoginCallbackHandler.class);
        props.put("sasl.jaas.config",  String.format(SAAS_JAAS_CONFIG, clientId, clientSecret,scope, clusterId, identityPool));
        props.put("sasl.mechanism", saslMechanism);
        props.put("security.protocol", securityProtocol);
        props.put("sasl.oauthbearer.token.endpoint.url", tokenEndpoint);

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
