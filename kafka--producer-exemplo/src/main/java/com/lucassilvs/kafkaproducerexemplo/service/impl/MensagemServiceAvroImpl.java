package com.lucassilvs.kafkaproducerexemplo.service.impl;

import com.lucassilvs.kafkaproducerexemplo.gateways.KafkaPostUtils;
import com.lucassilvs.kafkaproducerexemplo.gateways.kafka.UsuarioTesteAvro;
import com.lucassilvs.kafkaproducerexemplo.models.request.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"avro", "confluent", "oidc"})
@Service
public class MensagemServiceAvroImpl implements MensagemService {

    private final KafkaPostUtils kafkaPostUtils;

    private final String nomeTopico;

    @Autowired
    public MensagemServiceAvroImpl(KafkaPostUtils kafkaPostUtils, @Value("${spring.kafka.nome-topico}") String nomeTopico) {
        this.kafkaPostUtils = kafkaPostUtils;
        this.nomeTopico = nomeTopico;
    }

    public void postarMensagem(MensagemRequest mensagem) {

        UsuarioTesteAvro usuarioTesteAvro = UsuarioTesteAvro.newBuilder()
                .setId(1)
                .setNome(mensagem.getMensagem())
                .setSaldo(mensagem.getSaldo())
                .build();
        kafkaPostUtils.postarMensagem(usuarioTesteAvro , nomeTopico);
    }

}
