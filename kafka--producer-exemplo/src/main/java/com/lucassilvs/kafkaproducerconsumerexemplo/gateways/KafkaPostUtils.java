package com.lucassilvs.kafkaproducerconsumerexemplo.gateways;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaPostUtils {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaPostUtils(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(String mensagem, String nomeTopico){
        logger.info("Postando mensagem: " + mensagem + " no tópico " + nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);
        logger.info("mensagem enviada com sucesso");

    }
}
