package com.lucassilvs.kafkaproducerexemplo.controller.impl;


import com.lucassilvs.kafkaproducerexemplo.controller.MensagemController;
import com.lucassilvs.kafkaproducerexemplo.models.request.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagem")
public class MensagemControllerImpl  implements MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @PostMapping("/postar-mensagem")
    public ResponseEntity<Object> postarMensagem(MensagemRequest mensagem) {
        mensagemService.postarMensagem(mensagem.getMensagem());
        return ResponseEntity.ok("Mensagem postada com sucesso");
    }
}
