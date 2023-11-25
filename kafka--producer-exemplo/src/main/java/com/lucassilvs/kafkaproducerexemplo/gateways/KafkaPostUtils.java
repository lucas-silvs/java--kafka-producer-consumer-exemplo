package com.lucassilvs.kafkaproducerexemplo.gateways;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class KafkaPostUtils {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private final KafkaTemplate kafkaTemplate;

    private final KafkaTemplate kafkaTemplateTopico2;

    public KafkaPostUtils(KafkaTemplate kafkaTemplate, KafkaTemplate kafkaTemplateTopico2) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateTopico2 = kafkaTemplateTopico2;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: " + mensagem + " no tópico " + nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);

        kafkaTemplateTopico2.send("topico-teste-2", mensagem);

        logger.info("mensagem enviada com sucesso");
    }
}
