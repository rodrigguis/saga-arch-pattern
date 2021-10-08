package org.example.saga.order.service;

import org.example.saga.common.dto.OrderRequestDTO;
import org.example.saga.common.event.OrderEvent;
import org.example.saga.common.event.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusProducer {

    private final Sinks.Many<OrderEvent> orderSinks;

    @Autowired
    public OrderStatusProducer(Sinks.Many<OrderEvent> orderSinks) {
        this.orderSinks = orderSinks;
    }

    public void publishOrderEvent(OrderRequestDTO orderRequestDTO, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequestDTO, orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}

