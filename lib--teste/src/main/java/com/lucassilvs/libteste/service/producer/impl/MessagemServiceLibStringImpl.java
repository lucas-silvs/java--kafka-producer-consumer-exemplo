package com.lucassilvs.libteste.service.producer.impl;


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

    @Autowired
    private  KafkaTemplate<String, String> producer1;

    @Autowired
    public MessagemServiceLibStringImpl(KafkaTemplate<String, String> producer1) {
        this.producer1 = producer1;
    }

    public void postarMensagem(MensagemRequest mensagem) {
        producer1.send(nomeTopicoDefault, mensagem.getMensagem());
        System.out.println("PRODUCER - Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }

    @Override
    public void postarMensagem(MensagemRequest mensagem, String nomeTopico) {

        producer1.send(nomeTopico, mensagem.getMensagem());
        System.out.println("PRODUCER - Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());
    }
}
