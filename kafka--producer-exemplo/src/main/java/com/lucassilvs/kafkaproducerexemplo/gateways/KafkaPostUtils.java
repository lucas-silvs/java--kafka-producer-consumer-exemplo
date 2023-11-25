package com.lucassilvs.kafkaproducerexemplo.gateways;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaPostUtils {

    private Logger logger = LoggerFactory.getLogger(KafkaPostUtils.class);

    private final KafkaTemplate kafkaTemplate;

    private final KafkaTemplate kafkaTemplateTopico2;

    public KafkaPostUtils(KafkaTemplate kafkaTemplate, KafkaTemplate kafkaTemplateTopico2) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateTopico2 = kafkaTemplateTopico2;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: {} no tópico {}", mensagem, nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);

        kafkaTemplateTopico2.send("topico-teste-2", mensagem);

        logger.info("mensagem enviada com sucesso");
    }
}
