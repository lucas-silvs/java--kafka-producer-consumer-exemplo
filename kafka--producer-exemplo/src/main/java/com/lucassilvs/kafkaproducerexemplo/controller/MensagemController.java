package com.lucassilvs.kafkaproducerexemplo.controller;

import com.lucassilvs.kafkaproducerexemplo.models.request.MensagemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface MensagemController {
    ResponseEntity<Object> postarMensagem (@RequestBody MensagemRequest mensagem);
}
