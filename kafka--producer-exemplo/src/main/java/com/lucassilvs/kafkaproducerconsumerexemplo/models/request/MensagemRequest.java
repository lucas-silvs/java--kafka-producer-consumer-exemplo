package com.lucassilvs.kafkaproducerconsumerexemplo.models.request;


import org.springframework.lang.NonNull;

public class MensagemRequest {
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem( String mensagem) {
        this.mensagem = mensagem;
    }
}
