package com.lucassilvs.kafkaconsumerexemplo.controller.impl;

import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile({"avro", "confluent", "oidc"})
@Service
public class KafkaConsumerListenerAvroImpl  {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerListenerAvroImpl.class);

    @KafkaListener(topics = "${spring.kafka.nome-topico}", containerFactory = "kafkalisternerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumindoMensagemAvro(ConsumerRecord<String,UsuarioTesteAvro> mensagemSerialized) {
        UsuarioTesteAvro mensagem = mensagemSerialized.value();
        logger.info(String.format("Recebendo mensagem Avro - nome: %s id: %s saldo: %s", mensagem.getNome(), mensagem.getId(), mensagem.getSaldo()));
    }
}
