package com.lucassilvs.kafkaconsumerexemplo.entrypoint.mapper;

import com.lucassilvs.kafkaconsumerexemplo.entrypoint.model.OrderServiceModel;
import com.lucassilvs.kafkaconsumerexemplo.repositories.entities.OrderEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderServiceMapper {

    OrderServiceMapper INSTANCE = Mappers.getMapper(OrderServiceMapper.class);

    OrderEntity mapToEntity(OrderServiceModel order);

    @InheritInverseConfiguration
    OrderServiceModel mapToModel(OrderEntity order);
}
