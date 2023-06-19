package com.lucassilvs.libteste.service.impl;


import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.MensagemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("string")
public class MessagemServiceLibStringImpl implements MensagemService {

    @Value("${kafka.producers.producer1.nomeTopico}")
    private String nomeTopicoDefault;

    private  KafkaTemplate<String, String> kafkaTemplateMap;

    public void postarMensagem(MensagemRequest mensagem) {
        kafkaTemplateMap.send(nomeTopicoDefault, mensagem.getMensagem());
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }

    @Override
    public void postarMensagem(MensagemRequest mensagem, String nomeTopico) {

        kafkaTemplateMap.send(nomeTopico, mensagem.getMensagem());
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }
}
