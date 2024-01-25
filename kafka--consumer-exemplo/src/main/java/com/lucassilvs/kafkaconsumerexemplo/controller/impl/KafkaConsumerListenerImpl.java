package com.lucassilvs.kafkaconsumerexemplo.controller.impl;

import com.lucassilvs.kafkaconsumerexemplo.controller.KafkaConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile("string")
@Service
public class KafkaConsumerListenerImpl implements KafkaConsumerListener {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerListenerImpl.class);

    @KafkaListener(topics = "topico-teste", containerFactory = "consumer1")
    public void consumindoMensagemSimples(String mensagem) {
        logger.info("Recebendo mensagem topico 1:  {}", mensagem);

        System.out.println("Consumindo mensagem e gerando lag artificial");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Mensagem consumida");
    }

}
