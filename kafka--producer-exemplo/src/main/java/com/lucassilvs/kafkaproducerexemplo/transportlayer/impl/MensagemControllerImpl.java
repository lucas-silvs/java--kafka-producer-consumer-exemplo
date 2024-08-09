package com.lucassilvs.kafkaproducerexemplo.transportlayer.impl;


import com.lucassilvs.kafkaproducerexemplo.transportlayer.MensagemController;
import com.lucassilvs.kafkaproducerexemplo.transportlayer.models.MensagemRequest;
import com.lucassilvs.kafkaproducerexemplo.interactors.MensagemUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mensagem")
public class MensagemControllerImpl  implements MensagemController {

    private final MensagemUseCase mensagemUseCase;

    @Autowired
    public MensagemControllerImpl(MensagemUseCase mensagemUseCase) {
        this.mensagemUseCase = mensagemUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> postarMensagem(MensagemRequest mensagem) {
        mensagemUseCase.postarMensagem(mensagem);
        return ResponseEntity.ok("Mensagem postada com sucesso");
    }
}
