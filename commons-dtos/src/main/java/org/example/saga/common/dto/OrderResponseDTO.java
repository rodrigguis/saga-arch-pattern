package org.example.saga.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.saga.common.event.OrderStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private Integer userId;
    private Integer productId;
    private BigDecimal amount;
    private Integer orderId;
    private OrderStatus orderStatus;
}
