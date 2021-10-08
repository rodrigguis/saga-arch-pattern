package org.example.saga.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    private Integer userId;
    private Integer orderId;
    private BigDecimal amount;

}

