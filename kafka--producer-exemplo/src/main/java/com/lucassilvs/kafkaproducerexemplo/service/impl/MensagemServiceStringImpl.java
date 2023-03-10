package com.lucassilvs.kafkaproducerexemplo.service.impl;

import com.lucassilvs.kafkaproducerexemplo.gateways.KafkaPostUtils;
import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import com.lucassilvs.kafkaproducerexemplo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("string")
@Service
public class MensagemServiceStringImpl implements MensagemService {

    @Autowired
    private KafkaPostUtils kafkaPostUtils;

    @Value("${spring.kafka.nome-topico}")
    private String nomeTopico;


    public void postarMensagem(String mensagem) {

        kafkaPostUtils.postarMensagem(mensagem , nomeTopico);
    }




}
