package com.lucassilvs.kafkaconsumerexemplo.entrypoint;

import com.lucassilvs.kafkaconsumerexemplo.entrypoint.model.OrderServiceModel;

public interface OrderService {

    void createOrder(OrderServiceModel order);
}
