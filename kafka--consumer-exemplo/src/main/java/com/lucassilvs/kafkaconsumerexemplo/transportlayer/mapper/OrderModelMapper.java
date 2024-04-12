package com.lucassilvs.kafkaconsumerexemplo.transportlayer.mapper;

import com.lucassilvs.kafkaconsumerexemplo.entrypoint.model.OrderServiceModel;
import com.lucassilvs.kafkaconsumerexemplo.transportlayer.models.OrderModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderModelMapper {

    OrderModelMapper INSTANCE = Mappers.getMapper(OrderModelMapper.class);

    OrderServiceModel map(OrderModel orderModel);

    @InheritInverseConfiguration
    OrderModel map(OrderServiceModel orderServiceModel);
}
