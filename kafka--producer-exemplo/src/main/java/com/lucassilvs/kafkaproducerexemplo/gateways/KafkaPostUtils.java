package com.lucassilvs.kafkaproducerexemplo.gateways;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaPostUtils {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private final KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplate;

    public KafkaPostUtils(KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(UsuarioTesteAvro mensagem, String nomeTopico){
        logger.info("Postando mensagem: " + mensagem + " no tópico " + nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);
        logger.info("mensagem enviada com sucesso");

    }
}
