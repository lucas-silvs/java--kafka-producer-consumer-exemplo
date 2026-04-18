package com.lucassilvs.kafkaproducerexemplo.interactors.impl;

import com.lucassilvs.kafkaproducerexemplo.datasources.KafkaDataSourceImpl;
import com.lucassilvs.kafkaproducerexemplo.datasources.kafka.UsuarioTesteAvro;
import com.lucassilvs.kafkaproducerexemplo.interactors.MensagemUseCase;
import com.lucassilvs.kafkaproducerexemplo.repositories.EventRepository;
import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("string")
public class MensagemUseCaseStringImpl implements MensagemUseCase {

    private final EventRepository eventRepository;

    private final String nomeTopico;

    @Autowired
    public MensagemUseCaseStringImpl(KafkaDataSourceImpl kafkaDataSourceImpl, @Value("${spring.kafka.nome-topico}") String nomeTopico) {
        this.eventRepository = kafkaDataSourceImpl;
        this.nomeTopico = nomeTopico;
    }

    public void postarMensagem(MensagemRequest mensagem) {


        for (int i = 0; i < 10; i++) {

            eventRepository.sendEvent(mensagem.getMensagem(), nomeTopico);
        }
    }

}
