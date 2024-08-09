package com.lucassilvs.kafkaproducerexemplo.datasources;


import com.lucassilvs.kafkaproducerexemplo.repositories.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
public class KafkaDataSourceImpl implements EventRepository {

    private final Logger logger = LoggerFactory.getLogger(KafkaDataSourceImpl.class);

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaDataSourceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Object event, String destination){
        logger.info("Postando mensagem: {} no tópico {}", event, destination);
        CompletableFuture<SendResult<String, Object>> sendStatus = kafkaTemplate.send(destination, event);

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
