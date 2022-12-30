package com.lucassilvs.kafkaproducerconsumerexemplo.controller.impl;


import com.lucassilvs.kafkaproducerconsumerexemplo.controller.MensagemController;
import com.lucassilvs.kafkaproducerconsumerexemplo.models.request.MensagemRequest;
import com.lucassilvs.kafkaproducerconsumerexemplo.service.MensagemService;
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
