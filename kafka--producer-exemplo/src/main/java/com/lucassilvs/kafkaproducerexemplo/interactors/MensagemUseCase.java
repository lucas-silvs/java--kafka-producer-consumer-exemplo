package com.lucassilvs.kafkaproducerexemplo.interactors;

import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;

public interface MensagemUseCase {
     void postarMensagem (MensagemRequest mensagem);
}
