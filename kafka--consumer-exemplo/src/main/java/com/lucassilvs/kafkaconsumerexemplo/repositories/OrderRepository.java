package com.lucassilvs.kafkaconsumerexemplo.repositories;


import com.lucassilvs.kafkaconsumerexemplo.repositories.entities.OrderEntity;

public interface OrderRepository {

    void save(OrderEntity orderEntity);

    void delete(int orderId);

    OrderEntity findById(int orderId);

}
