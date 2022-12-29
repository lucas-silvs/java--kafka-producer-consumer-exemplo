package com.lucassilvs.kafkaproducerconsumerexemplo.service.impl;

import com.lucassilvs.kafkaproducerconsumerexemplo.gateways.KafkaPostUtils;
import com.lucassilvs.kafkaproducerconsumerexemplo.service.MensagemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MensagemServiceImpl implements MensagemService {

    private KafkaPostUtils kafkaPostUtils;

    @Value("${spring.kafka.nome-topico}")
    private String nomeTopico;
    public void postarMensagem(String mensagem) {
        kafkaPostUtils.postarMensagem(mensagem, nomeTopico);

    }
}
