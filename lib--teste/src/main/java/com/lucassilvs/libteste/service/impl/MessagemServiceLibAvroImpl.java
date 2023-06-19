package com.lucassilvs.libteste.service.impl;


import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import com.lucassilvs.libteste.lib.ProducerListComponent;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.MensagemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("avro")
public class MessagemServiceLibAvroImpl implements MensagemService {

    @Value("${kafka.producers.producer1.nomeTopico}")
    private String nomeTopicoDefault;

    private  KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplateMap;




    public void postarMensagem(MensagemRequest mensagem) {
        if (kafkaTemplateMap == null) {
            kafkaTemplateMap = ProducerListComponent.buscaProducer("producer2");
        }
        UsuarioTesteAvro testeAvro = new UsuarioTesteAvro(1, mensagem.getMensagem(), mensagem.getSaldo());


        kafkaTemplateMap.send(nomeTopicoDefault, testeAvro);
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());

    }

    public void postarMensagem(MensagemRequest mensagem, String nomeTopico) {
        if (kafkaTemplateMap == null) {
            kafkaTemplateMap = ProducerListComponent.buscaProducer("producer2");
        }
        UsuarioTesteAvro testeAvro = new UsuarioTesteAvro(1, mensagem.getMensagem(), mensagem.getSaldo());


        kafkaTemplateMap.send(nomeTopicoDefault, testeAvro);
        System.out.println("Mensagem: " + mensagem.getMensagem() + " Saldo: " + mensagem.getSaldo());

    }
}
