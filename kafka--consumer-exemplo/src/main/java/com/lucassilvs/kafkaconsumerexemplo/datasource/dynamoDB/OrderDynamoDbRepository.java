package com.lucassilvs.kafkaconsumerexemplo.datasource.dynamoDB;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface OrderDynamoDbRepository extends CrudRepository<OrderDynamoDbEntity, Integer> {

    Optional<OrderDynamoDbEntity> findById(Integer id);

}