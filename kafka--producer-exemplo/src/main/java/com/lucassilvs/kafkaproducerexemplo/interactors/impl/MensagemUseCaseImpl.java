package com.lucassilvs.kafkaproducerexemplo.interactors.impl;

import com.lucassilvs.kafkaproducerexemplo.datasources.KafkaDataSourceImpl;
import com.lucassilvs.kafkaproducerexemplo.datasources.kafka.UsuarioTesteAvro;
import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.interactors.MensagemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"avro", "confluent", "oidc"})
@Service
public class MensagemUseCaseImpl implements MensagemUseCase {

    private final KafkaDataSourceImpl kafkaDataSourceImpl;

    private final String nomeTopico;

    @Autowired
    public MensagemUseCaseImpl(KafkaDataSourceImpl kafkaDataSourceImpl, @Value("${spring.kafka.nome-topico}") String nomeTopico) {
        this.kafkaDataSourceImpl = kafkaDataSourceImpl;
        this.nomeTopico = nomeTopico;
    }

    public void postarMensagem(MensagemRequest mensagem) {


        for (int i = 0; i < 10; i++) {
            UsuarioTesteAvro usuarioTesteAvro = UsuarioTesteAvro.newBuilder()
                    .setId(i)
                    .setNome(mensagem.getMensagem())
                    .setSaldo(mensagem.getSaldo())
                    .build();
            kafkaDataSourceImpl.sendEvent(usuarioTesteAvro, nomeTopico);
        }
    }

}
