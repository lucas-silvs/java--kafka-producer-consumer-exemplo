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

import java.util.Map;

@Configuration
@Profile("confluent")
@EnableKafka
public class KafkaConsumerConfigConfluent {


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
    public ConsumerFactory<String, SpecificRecord> consumerFactory(final KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        //Autenticação Cluster Kafka
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(SAAS_JAAS_CONFIG, clusterCredentialsKey, clusterCredentialsPassword));

        //Autenticação Schema Registry
        props.put(AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG, String.format("%s:%s",schemaRegistryUserinforUsername,schemaRegistryUserinforPassword));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,SpecificRecord>> kafkalisternerContainerFactory(ConsumerFactory<String, SpecificRecord> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,SpecificRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }


}
