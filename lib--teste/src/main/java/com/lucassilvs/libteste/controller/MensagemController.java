package com.lucassilvs.libteste.controller;

import com.lucassilvs.libteste.request.MensagemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface MensagemController {
    ResponseEntity<Object> postarMensagem (@RequestBody MensagemRequest mensagem);
}
