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
public class ProducerConfigConfluentOIDCProvider {

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

    @Value("${security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    private  static  final String SAAS_JAAS_CONFIG2 = "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required " +
            "clientId=\"%s\" " +
            "clientSecret=\"%s\" " +
            "scope=\".default\" " +
            "extension_logicalCluster=\"lkc-oqw1mj\" " +
            "extension_identityPoolId=\"pool-ve8k\";";

    @Value("${spring.kafka.properties.sasl.clientId}")
    private String  clientId;

    @Value("${spring.kafka.properties.sasl.clientSecret}")
    private String clientSecret;

    @Value("${spring.kafka.properties.sasl.oauthbearer.token.endpoint.url}")
    private String tokenEndpoint;

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
        props.put("sasl.jaas.config",  String.format(SAAS_JAAS_CONFIG2, clientId, clientSecret));
        props.put("sasl.mechanism", saslMechanism);
        props.put("security.protocol", securityProtocol);
        props.put("sasl.oauthbearer.token.endpoint.url", tokenEndpoint);

        //Utilizando AVRO
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        props.put(KafkaAvroSerializerConfig.AVRO_REMOVE_JAVA_PROPS_CONFIG, true);
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
