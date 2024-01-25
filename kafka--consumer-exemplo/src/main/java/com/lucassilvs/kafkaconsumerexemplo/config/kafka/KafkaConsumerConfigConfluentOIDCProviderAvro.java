package com.lucassilvs.kafkaconsumerexemplo.config.kafka;


import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.time.Duration;
import java.util.Map;

@Configuration
@Profile("oidc")
@EnableKafka
public class KafkaConsumerConfigConfluentOIDCProviderAvro {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.timeout}")
    private Integer timeOut;

    @Value("${spring.kafka.schema-registry.url}")
    private String schemaRegistry;

    @Value("${spring.kafka.properties.basic.auth.credentials.source}")
    private String schemaRegistryCredentialsSource;

    @Value("${security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.basic.auth.user.info.username}")
    private String schemaRegistryUserinforUsername;

    @Value("${spring.kafka.properties.basic.auth.user.info.password}")
    private String schemaRegistryUserinforPassword;

    @Value("${spring.application.name}")
    private String consumerClientId;

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

    private  static  final String OAUTHBEARER_JAAS_CONFIG = "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required " +
            "clientId=\"%s\" " +
            "clientSecret=\"%s\" " +
            "scope=\"%s\" " +
            "extension_logicalCluster=\"%s\" " +
            "extension_identityPoolId=\"%s\";";

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        //Autenticação Cluster Kafka
        props.put(SaslConfigs.SASL_JAAS_CONFIG,  String.format(OAUTHBEARER_JAAS_CONFIG, clientId, clientSecret,scope, clusterId, identityPool));

        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,SpecificRecord>> kafkalisternerContainerFactory(ConsumerFactory<String, Object> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,SpecificRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));

        return factory;
    }


}
