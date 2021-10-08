package org.example.saga.order.config;

import org.example.saga.common.dto.OrderRequestDTO;
import org.example.saga.common.event.OrderStatus;
import org.example.saga.common.event.PaymentStatus;
import org.example.saga.order.entity.PurchaseOrder;
import org.example.saga.order.repository.OrderRepository;
import org.example.saga.order.service.OrderStatusProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderStatusProducer producer;

    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
        repository.findById(id).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {
        final var isPaymentComplete =
                PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());

        final var orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);

        if (!isPaymentComplete)
            producer.publishOrderEvent(convertEntityToDTO(purchaseOrder), orderStatus);

    }

    public OrderRequestDTO convertEntityToDTO(PurchaseOrder purchaseOrder) {
        return OrderRequestDTO.builder()
                .orderId(purchaseOrder.getId())
                .userId(purchaseOrder.getUserId())
                .amount(purchaseOrder.getPrice())
                .productId(purchaseOrder.getProductId())
                .build();
    }
}
