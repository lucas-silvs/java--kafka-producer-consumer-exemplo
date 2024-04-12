package com.lucassilvs.kafkaconsumerexemplo.repositories.mapper;

import com.lucassilvs.kafkaconsumerexemplo.datasource.dynamoDB.OrderDynamoDbEntity;
import com.lucassilvs.kafkaconsumerexemplo.repositories.entities.OrderEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderEntityMapper {

    OrderEntityMapper INSTANCE = Mappers.getMapper(OrderEntityMapper.class);

    OrderDynamoDbEntity map(OrderEntity orderEntity);

    @InheritInverseConfiguration
    OrderEntity map(OrderDynamoDbEntity orderDynamoDbEntity);

}
