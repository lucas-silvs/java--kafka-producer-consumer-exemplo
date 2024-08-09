package com.lucassilvs.kafkaproducerexemplo.transportlayer;

import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface MensagemController {
    ResponseEntity<Object> postarMensagem (@RequestBody MensagemRequest mensagem);
}
