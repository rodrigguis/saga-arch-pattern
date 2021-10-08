package org.example.saga.common.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderRequestDTO {

    private Integer userId;
    private Integer productId;
    private BigDecimal amount;
    private Integer orderId;
}
