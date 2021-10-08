package org.example.saga.order.config;

import org.example.saga.common.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        /*
        * TODO: Listen payment-event-topic
        *  will check payment status
        *  if payment status completed -> complete the order
        *  if payment status failed -> cancel the order*/

        return (paymentEvent ->
                handler.updateOrder(paymentEvent.getPaymentRequestDTO().getOrderId(),
                        purchaseOrder ->
                                purchaseOrder.setPaymentStatus(paymentEvent.getPaymentStatus())));
    }
}
