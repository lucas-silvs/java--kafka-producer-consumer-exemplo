package com.lucassilvs.kafkaconsumerexemplo.transportlayer;



public interface KafkaConsumerListener<T> {

    void consumindoMensagemSimples(T mensagem);



}
