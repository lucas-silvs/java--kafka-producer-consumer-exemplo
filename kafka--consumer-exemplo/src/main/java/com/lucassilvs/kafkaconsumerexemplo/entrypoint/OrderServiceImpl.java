package com.lucassilvs.kafkaconsumerexemplo.entrypoint;

import com.lucassilvs.kafkaconsumerexemplo.entrypoint.mapper.OrderServiceMapper;
import com.lucassilvs.kafkaconsumerexemplo.entrypoint.model.OrderServiceModel;
import com.lucassilvs.kafkaconsumerexemplo.repositories.OrderRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;

    private final OrderServiceMapper orderServiceMapper = OrderServiceMapper.INSTANCE;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public void createOrder(OrderServiceModel order) {
        repository.save(orderServiceMapper.mapToEntity(order));
    }

    public OrderServiceModel getOrder(int orderId) {
        return orderServiceMapper.mapToModel(repository.findById(orderId));
    }
}
