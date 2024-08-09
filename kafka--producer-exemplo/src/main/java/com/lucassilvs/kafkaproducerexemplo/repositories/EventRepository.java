package com.lucassilvs.kafkaproducerexemplo.repositories;

public interface EventRepository {

    void sendEvent(Object event, String destination);
}
