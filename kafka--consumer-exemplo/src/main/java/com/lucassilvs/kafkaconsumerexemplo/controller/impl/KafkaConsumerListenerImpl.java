package com.lucassilvs.kafkaconsumerexemplo.controller.impl;

import com.lucassilvs.kafkaconsumerexemplo.controller.KafkaConsumerListener;
import com.lucassilvs.kafkaconsumerexemplo.models.OrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile({"string",  "json"})
@Service
public class KafkaConsumerListenerImpl implements KafkaConsumerListener<OrderModel> {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerListenerImpl.class);



    @Profile("json")
    @KafkaListener(topics = "teste-topico-json", containerFactory = "consumerFactory")
    public void consumindoMensagemSimples(OrderModel mensagem) {
        logger.info("Recebendo mensagem topico com JSON:  {}", mensagem);

        System.out.println("Mensagem consumida");
    }

}
