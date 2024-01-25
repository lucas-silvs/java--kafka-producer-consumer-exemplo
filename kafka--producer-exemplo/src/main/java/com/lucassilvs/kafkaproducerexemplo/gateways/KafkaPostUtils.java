package com.lucassilvs.kafkaproducerexemplo.gateways;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class KafkaPostUtils {

    private final Logger logger = LoggerFactory.getLogger(KafkaPostUtils.class);

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaPostUtils(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: {} no tópico {}", mensagem, nomeTopico);
        CompletableFuture<SendResult<String, Object>> sendStatus = kafkaTemplate.send(nomeTopico, mensagem);

        sendStatus.whenComplete((result, exception) -> {
            if (exception != null) {
                logger.error("Erro ao enviar mensagem: {}", exception.getMessage());
            } else {
                logger.info("Mensagem enviada com sucesso:\nTopico: {} - particao: {} - offset: {}", result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
        }
}
