package com.lucassilvs.kafkaproducerexemplo.gateways;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaPostUtils {

    private Logger logger = LoggerFactory.getLogger(KafkaPostUtils.class);

    private final KafkaTemplate kafkaTemplate;


    public KafkaPostUtils(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: {} no tópico {}", mensagem, nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);

        logger.info("mensagem enviada com sucesso");
    }
}
