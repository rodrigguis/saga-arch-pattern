package org.example.saga.order.service;

import org.example.saga.common.dto.OrderRequestDTO;
import org.example.saga.common.event.OrderStatus;
import org.example.saga.order.entity.PurchaseOrder;
import org.example.saga.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {

        private final OrderRepository purchaseOrderRepository;
        private final OrderStatusProducer orderStatusProducer;

        @Autowired
        public OrderService(OrderRepository purchaseOrderRepository, OrderStatusProducer orderStatusProducer) {
            this.purchaseOrderRepository = purchaseOrderRepository;
            this.orderStatusProducer = orderStatusProducer;
        }

        @Transactional
        public PurchaseOrder createOrder(OrderRequestDTO dto) {
            final var order = purchaseOrderRepository.save(convertDtoToEntity(dto));
            dto.setOrderId(order.getId());

            orderStatusProducer.publishOrderEvent(dto, OrderStatus.ORDER_CREATED);

            return order;

        }

        public List<PurchaseOrder> getAllOrders() {
            return purchaseOrderRepository.findAll();
        }

        private PurchaseOrder convertDtoToEntity(OrderRequestDTO dto) {
            return PurchaseOrder.builder()
                    .userId(dto.getUserId())
                    .orderStatus(OrderStatus.ORDER_CREATED)
                    .productId(dto.getProductId())
                    .price(dto.getAmount())
                    .build();
        }
}
