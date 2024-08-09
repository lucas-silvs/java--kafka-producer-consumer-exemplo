package com.lucassilvs.kafkaproducerexemplo.interactors.impl;

import com.lucassilvs.kafkaproducerexemplo.datasources.KafkaDataSourceImpl;
import com.lucassilvs.kafkaproducerexemplo.datasources.kafka.UsuarioTesteAvro;
import com.lucassilvs.kafkaproducerexemplo.repositories.EventRepository;
import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.interactors.MensagemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MensagemUseCaseImpl implements MensagemUseCase {

    private final EventRepository eventRepository;

    private final String nomeTopico;

    @Autowired
    public MensagemUseCaseImpl(KafkaDataSourceImpl kafkaDataSourceImpl, @Value("${spring.kafka.nome-topico}") String nomeTopico) {
        this.eventRepository = kafkaDataSourceImpl;
        this.nomeTopico = nomeTopico;
    }

    public void postarMensagem(MensagemRequest mensagem) {


        for (int i = 0; i < 10; i++) {
            UsuarioTesteAvro usuarioTesteAvro = UsuarioTesteAvro.newBuilder()
                    .setId(i)
                    .setNome(mensagem.getMensagem())
                    .setSaldo(mensagem.getSaldo())
                    .build();
            eventRepository.sendEvent(usuarioTesteAvro, nomeTopico);
        }
    }

}
