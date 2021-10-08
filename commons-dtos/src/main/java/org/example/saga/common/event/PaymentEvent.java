package org.example.saga.common.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.saga.common.dto.PaymentRequestDTO;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@Data
public class PaymentEvent implements Event {

    private UUID eventId = UUID.randomUUID();
    private LocalDate eventDate = LocalDate.now();
    private PaymentRequestDTO paymentRequestDTO;
    private PaymentStatus paymentStatus;

    public PaymentEvent(PaymentRequestDTO paymentRequestDTO, PaymentStatus paymentStatus) {
        this.paymentRequestDTO = paymentRequestDTO;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public LocalDate getEventDate() {
        return this.eventDate;
    }
}