package com.lucassilvs.kafkaconsumerexemplo.repositories;

import com.lucassilvs.kafkaconsumerexemplo.datasource.dynamoDB.OrderDynamoDbRepository;
import com.lucassilvs.kafkaconsumerexemplo.repositories.entities.OrderEntity;
import com.lucassilvs.kafkaconsumerexemplo.repositories.mapper.OrderEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderRepositoryImpl implements OrderRepository{

    private final OrderDynamoDbRepository orderDynamoDbRepository;

    private final OrderEntityMapper entityMapper = OrderEntityMapper.INSTANCE;

    @Autowired
    public OrderRepositoryImpl(OrderDynamoDbRepository orderDynamoDbRepository) {
        this.orderDynamoDbRepository = orderDynamoDbRepository;
    }

    @Override
    public void save(OrderEntity orderEntity) {
        orderDynamoDbRepository.save(entityMapper.map(orderEntity));
    }

    @Override
    public void delete(int orderId) {

        orderDynamoDbRepository.findById(orderId).ifPresentOrElse(orderDynamoDbRepository::delete, () -> {
            throw new RuntimeException("Order not found");
        });
    }

    @Override
    public OrderEntity findById(int orderId) {

        return orderDynamoDbRepository.findById(orderId)
                .map(entityMapper::map)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
