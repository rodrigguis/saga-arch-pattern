package org.example.saga.common.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.saga.common.dto.OrderRequestDTO;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderEvent implements Event {

    private UUID eventId = UUID.randomUUID();
    private LocalDate eventDate = LocalDate.now();

    public OrderEvent(OrderRequestDTO orderRequestDTO, OrderStatus orderStatus) {
        this.orderRequestDTO = orderRequestDTO;
        this.orderStatus = orderStatus;
    }

    private OrderRequestDTO orderRequestDTO;
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public LocalDate getEventDate() {
        return this.eventDate;
    }
}
