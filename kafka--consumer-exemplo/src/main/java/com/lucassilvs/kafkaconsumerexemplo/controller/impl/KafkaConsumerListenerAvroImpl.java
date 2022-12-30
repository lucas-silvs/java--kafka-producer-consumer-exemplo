package com.lucassilvs.kafkaconsumerexemplo.controller.impl;

import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile("avro")
@Service
public class KafkaConsumerListenerAvroImpl  {

    @KafkaListener(topics = "${spring.kafka.nome-topico}", containerFactory = "kafkalisternerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumindoMensagemAvro(ConsumerRecord<String,UsuarioTesteAvro> mensagemSerialized) {
        UsuarioTesteAvro mensagem = mensagemSerialized.value();
        System.out.println("Recebendo mensagem Avro - nome: " + mensagem.getNome() + " id: " + mensagem.getId() + " saldo: " + mensagem.getSaldo());
    }
}
