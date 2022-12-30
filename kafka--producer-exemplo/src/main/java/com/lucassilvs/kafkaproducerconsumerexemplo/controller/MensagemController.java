package com.lucassilvs.kafkaproducerconsumerexemplo.controller;

import com.lucassilvs.kafkaproducerconsumerexemplo.models.request.MensagemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface MensagemController {
    ResponseEntity<Object> postarMensagem (@RequestBody MensagemRequest mensagem);
}
