package com.lucassilvs.kafkaconsumerexemplo.transportlayer.impl;

import com.lucassilvs.kafkaconsumerexemplo.entrypoint.OrderService;
import com.lucassilvs.kafkaconsumerexemplo.transportlayer.KafkaConsumerListener;
import com.lucassilvs.kafkaconsumerexemplo.transportlayer.mapper.OrderModelMapper;
import com.lucassilvs.kafkaconsumerexemplo.transportlayer.models.OrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile({"string",  "json"})
@Service
public class KafkaConsumerListenerImpl implements KafkaConsumerListener<OrderModel> {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerListenerImpl.class);

    private final OrderService orderService;

    private final OrderModelMapper orderModelMapper = OrderModelMapper.INSTANCE;

    public KafkaConsumerListenerImpl(OrderService orderService) {
        this.orderService = orderService;
    }


    @Profile("json")
    @KafkaListener(topics = "teste-topico-json", containerFactory = "consumerFactory")
    public void consumindoMensagemSimples(OrderModel mensagem) {
        logger.info("Recebendo mensagem topico com JSON:  {}", mensagem);

        logger.info("Salvando mensagem no banco de dados -- orderid: {}", mensagem.getOrderId());

        orderService.createOrder(orderModelMapper.map(mensagem));

        System.out.println("Mensagem consumida");
    }

}
