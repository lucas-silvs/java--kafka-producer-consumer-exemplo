package com.lucassilvs.kafkaconsumerexemplo.transportlayer;



public interface KafkaConsumerListener<T>{

    void consumirMensagem(T mensagem);



}
