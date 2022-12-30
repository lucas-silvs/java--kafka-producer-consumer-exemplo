package com.lucassilvs.kafkaconsumerexemplo.controller;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumerListener {

    void consumindoMensagemSimples(String mensagem);



}
