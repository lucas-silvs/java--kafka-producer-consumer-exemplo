package com.lucassilvs.kafkaconsumerexemplo.controller;



public interface KafkaConsumerListener<T> {

    void consumindoMensagemSimples(T mensagem);



}
