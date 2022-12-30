package com.lucassilvs.kafkaconsumerexemplo.controller.impl;

import com.lucassilvs.kafkaconsumerexemplo.controller.KafkaConsumerListener;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile("string")
@Service
public class KafkaConsumerListenerImpl implements KafkaConsumerListener {

    @KafkaListener(topics = "${spring.kafka.nome-topico}", containerFactory = "kafkalisternerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumindoMensagemSimples(String mensagem) {
        System.out.println("Recebendo mensagem: " + mensagem);
    }


}
