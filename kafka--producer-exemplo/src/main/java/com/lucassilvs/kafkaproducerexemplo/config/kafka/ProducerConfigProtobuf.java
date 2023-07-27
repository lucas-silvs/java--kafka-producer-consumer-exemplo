package com.lucassilvs.kafkaproducerexemplo.config.kafka;

import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Profile("protobuf")
@Configuration
public class ProducerConfigProtobuf {

    @Bean
    public Map<String, Object> producerConfigs(KafkaProperties kafkaProperties) {
        Map<String, Object> props =  kafkaProperties.buildProducerProperties();

        //Utilizando AVRO
        return props;
    }

    @Bean
    public ProducerFactory<String, UsuarioTesteAvro> producerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties));
    }

    @Bean
    public KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplate(final KafkaProperties kafkaProperties){
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }


}
