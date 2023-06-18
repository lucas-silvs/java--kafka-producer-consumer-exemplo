package com.lucassilvs.libteste.service.impl;


import com.lucassilvs.libkafkaclients.clients.producer.ProducerMap;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MessagemServiceLibImpl implements MensagemService {



    @Autowired
    private Map<String, KafkaTemplate> producers;

    public void postarMensagem(MensagemRequest mensagem) {
        KafkaTemplate kafkaTemplate = producers.get("producer1");
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());

    }
}
