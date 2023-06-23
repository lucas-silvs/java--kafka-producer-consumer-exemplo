package com.lucassilvs.libteste.service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("avro")
public class ConsumerListenerAvro {

    @KafkaListener(
            topics = "${kafka.consumers.consumer2.nomeTopico}",
            containerFactory = "consumer2",
            groupId = "${kafka.consumers.consumer2.groupId}"
    )
    public void consumirMensagem1(ConsumerRecord<String, Object> mensagem) {
        Object mensagemConsumida = mensagem.value();
        System.out.println("CONSUMER AVRO -------- Mensagem consumida: " + mensagemConsumida );

    }
}
