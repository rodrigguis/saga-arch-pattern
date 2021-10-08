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

@Getter
@Setter
@Table(name = "user_balance")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserBalance {

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    private BigDecimal price;

}