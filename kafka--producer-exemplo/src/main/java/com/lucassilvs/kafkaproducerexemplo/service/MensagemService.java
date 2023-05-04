package com.lucassilvs.kafkaproducerexemplo.service;

import com.lucassilvs.kafkaproducerexemplo.models.request.MensagemRequest;

public interface MensagemService {
     void postarMensagem (MensagemRequest mensagem);
}
