package com.lucassilvs.libteste.service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("string")
public class ConsumerListenerString  {

    @KafkaListener(
            topics = "${kafka.consumers.consumer1.nomeTopico}",
            containerFactory = "consumer1",
            groupId = "${kafka.consumers.consumer1.groupId}"
    )
    public void consumirMensagem1(ConsumerRecord<String, String> mensagem) {
        String mensagemConsumida = mensagem.value();
        System.out.println("CONSUMER STRING -------- Mensagem consumida: " + mensagemConsumida);
    }
}







