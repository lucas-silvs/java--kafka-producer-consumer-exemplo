package com.lucassilvs.kafkaconsumerexemplo.config.kafka;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Profile("oidc")
@Configuration
public class ConsumerConfiguratorOidc implements KafkaPropertiesConfigurator {

    @Value("${spring.kafka.properties.basic.auth.user.info.username}")
    private String schemaRegistryUserinforUsername;

    @Value("${spring.kafka.properties.basic.auth.user.info.password}")
    private String schemaRegistryUserinforPassword;


    @Value("${extension.cluster}")
    private String clusterId;

    @Value("${extension.identity-pool}")
    private String identityPool;

    @Value("${spring.kafka.properties.sasl.clientId}")
    private String  clientId;

    @Value("${spring.kafka.properties.sasl.clientSecret}")
    private String clientSecret;

    @Value("${spring.kafka.properties.sasl.scope}")
    private String scope;

    private  static  final String OAUTHBEARER_JAAS_CONFIG = "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required " +
            "clientId=\"%s\" " +
            "clientSecret=\"%s\" " +
            "scope=\"%s\" " +
            "extension_logicalCluster=\"%s\" " +
            "extension_identityPoolId=\"%s\";";



    @Override
    public void configure(Map<String, Object> props) {
        props.put(SaslConfigs.SASL_JAAS_CONFIG,  String.format(OAUTHBEARER_JAAS_CONFIG, clientId, clientSecret, scope, clusterId, identityPool));

        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));
    }
}
