package com.lucassilvs.kafkaproducerexemplo.gateways;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaPostUtils {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private final KafkaTemplate kafkaTemplate;

    public KafkaPostUtils(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: " + mensagem + " no tópico " + nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);
        logger.info("mensagem enviada com sucesso");
    }
}
