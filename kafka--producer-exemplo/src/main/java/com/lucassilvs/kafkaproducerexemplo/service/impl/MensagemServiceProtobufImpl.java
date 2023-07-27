package com.lucassilvs.kafkaproducerexemplo.service.impl;

import com.lucassilvs.kafkaproducerexemplo.gateways.KafkaPostUtils;
import com.lucassilvs.kafkaproducerexemplo.models.request.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.service.MensagemService;
import io.confluent.cloud.demo.domain.UsuarioTesteProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("protobuf")
@Service
public class MensagemServiceProtobufImpl implements MensagemService {

    @Autowired
    private KafkaPostUtils kafkaPostUtils;

    @Value("${spring.kafka.nome-topico}")
    private String nomeTopico;


    public void postarMensagem(MensagemRequest mensagem) {
        UsuarioTesteProto.mensagem usuarioTesteProto = UsuarioTesteProto.mensagem.newBuilder()
                        .setNome("teste").build();
        kafkaPostUtils.postarMensagem(usuarioTesteProto , nomeTopico);
    }




}
