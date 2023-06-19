package com.lucassilvs.libteste.controller.impl;

import com.lucassilvs.libteste.controller.MensagemController;
import com.lucassilvs.libteste.request.MensagemRequest;
import com.lucassilvs.libteste.service.producer.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagem/{topicName}")
public class MensagemControllerImpl implements MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<Object> postarMensagem(@PathVariable("topicName") String topicName, MensagemRequest mensagem) {
        mensagemService.postarMensagem(mensagem, topicName);
        return ResponseEntity.ok("Mensagem postada com sucesso");
    }
}
