package com.lucassilvs.libteste.service.impl;


import com.lucassilvs.libteste.lib.ProducerListComponent;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MessagemServiceLibImpl implements MensagemService {


    // injeta a lista de producers do bean listaKafkaTemplate
    @Autowired
    private ProducerListComponent listaKafkaTemplate;

    public void postarMensagem(MensagemRequest mensagem) {
        Map<String, KafkaTemplate> kafkaTemplateMap = listaKafkaTemplate.listaKafkaTemplate();
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());

    }
}
