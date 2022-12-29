package com.lucassilvs.kafkaproducerconsumerexemplo.gateways;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPostUtils {

    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaPostUtils(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void postarMensagem(String mensagem, String nomeTopico){
        kafkaTemplate.send(nomeTopico, mensagem);
    }
}
