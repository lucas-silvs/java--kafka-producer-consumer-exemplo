package com.lucassilvs.libteste.request;


public class MensagemRequest {
    private String mensagem;

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
