package org.example.saga.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_transaction")
@Entity
public class UserTransaction {

    @Id
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    private Integer userId;

    private BigDecimal amount;


}