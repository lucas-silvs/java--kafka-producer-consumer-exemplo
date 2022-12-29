package com.lucassilvs.kafkaproducerconsumerexemplo.controller.impl;


import com.lucassilvs.kafkaproducerconsumerexemplo.controller.MensagemController;
import com.lucassilvs.kafkaproducerconsumerexemplo.models.request.MensagemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagem")
public class MensagemControllerImpl  implements MensagemController {


    @PostMapping("/postar-mensagem")
    public ResponseEntity<Object> postarMensagem(MensagemRequest mensagem) {
        return ResponseEntity.ok("Teste");
    }
}
