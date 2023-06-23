package com.lucassilvs.libteste.service.producer.impl;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import com.lucassilvs.libkafkaclients.annotation.producer.KafkaProducer;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.producer.MensagemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("avro")
public class MessagemServiceLibAvroImpl implements MensagemService {

    @Value("${kafka.producers.producer2.nomeTopico}")
    private String nomeTopicoDefault;


    @KafkaProducer("producer2")
    private KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplateMap;


    public void postarMensagem(MensagemRequest mensagem) {
        UsuarioTesteAvro testeAvro = UsuarioTesteAvro.newBuilder()
                .setId(1)
                .setNome(mensagem.getMensagem())
                .setSaldo(mensagem.getSaldo())
                .build();

        kafkaTemplateMap.send(nomeTopicoDefault, testeAvro);
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());

    }

    public void postarMensagem(MensagemRequest mensagem, String nomeTopico) {
        UsuarioTesteAvro testeAvro = UsuarioTesteAvro.newBuilder()
                .setId(1)
                .setNome(mensagem.getMensagem())
                .setSaldo(mensagem.getSaldo())
                .build();

        kafkaTemplateMap.send(nomeTopico, testeAvro);
    }
}
