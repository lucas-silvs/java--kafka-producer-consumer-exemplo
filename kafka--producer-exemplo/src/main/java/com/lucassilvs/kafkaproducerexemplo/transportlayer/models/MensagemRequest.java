package com.lucassilvs.kafkaproducerexemplo.transportlayer.models;


public class MensagemRequest {
    private String mensagem = "teste";

    private String saldo = "1000";

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem( String mensagem) {
        this.mensagem = mensagem;
    }
}
