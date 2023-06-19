package com.lucassilvs.libteste.service.producer.impl;


import com.lucassilvs.libkafkaclients.KafkaClientsManager;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.producer.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("string")
public class MessagemServiceLibStringImpl implements MensagemService {

    @Value("${kafka.producers.producer1.nomeTopico}")
    private String nomeTopicoDefault;

    private  KafkaTemplate<String, String> producerTeste;

    @Autowired
    public MessagemServiceLibStringImpl(KafkaClientsManager kafkaClientsManager) {
        this.producerTeste = kafkaClientsManager.buscaProducer("producer1");
    }

    public void postarMensagem(MensagemRequest mensagem) {
        producerTeste.send(nomeTopicoDefault, mensagem.getMensagem());
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }

    @Override
    public void postarMensagem(MensagemRequest mensagem, String nomeTopico) {

        producerTeste.send(nomeTopico, mensagem.getMensagem());
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }
}