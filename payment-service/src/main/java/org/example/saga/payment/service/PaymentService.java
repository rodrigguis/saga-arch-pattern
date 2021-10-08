package org.example.saga.payment.service;

import org.example.saga.common.dto.OrderRequestDTO;
import org.example.saga.common.dto.PaymentRequestDTO;
import org.example.saga.common.event.OrderEvent;
import org.example.saga.common.event.PaymentEvent;
import org.example.saga.common.event.PaymentStatus;
import org.example.saga.payment.entity.UserBalance;
import org.example.saga.payment.entity.UserTransaction;
import org.example.saga.payment.repository.UserBalanceRepository;
import org.example.saga.payment.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    private final UserBalanceRepository userBalanceRepository;
    private final UserTransactionRepository userTransactionRepository;

    @Autowired
    public PaymentService(UserBalanceRepository userBalanceRepository, UserTransactionRepository userTransactionRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.userTransactionRepository = userTransactionRepository;
    }

    @PostConstruct
    public void initUserBalanceInDB() {
        userBalanceRepository.saveAll(
                Stream.of(new UserBalance(101, new BigDecimal(5000)),
                          new UserBalance(102, new BigDecimal(3000)),
                          new UserBalance(103, new BigDecimal(4200)),
                          new UserBalance(104, new BigDecimal(20000)),
                          new UserBalance(105, new BigDecimal(999)))
                        .collect(Collectors.toList()));
    }

//    @Transactional
//    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
//        OrderRequestDTO orderRequestDTO = orderEvent.getOrderRequestDTO();
//        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderRequestDTO.getOrderId(),
//                orderRequestDTO.getOrderId(), orderEvent.getOrderRequestDTO().getAmount());
//
//        //TODO: Verify this lambda, because I not be believe this
//        return userBalanceRepository.findById(orderRequestDTO.getUserId())
//                .filter(ub -> ub.getPrice() > orderRequestDTO.getAmount())
//                .map(ub -> {
//                    ub.setPrice(ub.getPrice() - orderRequestDTO.getAmount());
//                    userTransactionRepository.save(new UserTransaction(orderRequestDTO.getOrderId(),
//                            orderRequestDTO.getUserId(),
//                            orderEvent.getOrderRequestDTO().getAmount()));
//
//                    return new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_COMPLETED);
//                }).orElse(new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_FAILED));
//
//    }

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDTO orderRequestDTO = orderEvent.getOrderRequestDTO();
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderRequestDTO.getOrderId(),
                orderRequestDTO.getOrderId(), orderEvent.getOrderRequestDTO().getAmount());

        //TODO: Verify this lambda, because I not be believe this
        return userBalanceRepository.findById(orderRequestDTO.getUserId())
                .filter(ub -> {
                    if (ub.getPrice().compareTo(orderRequestDTO.getAmount()) > -1) {
                        ub.setPrice(ub.getPrice().subtract(orderRequestDTO.getAmount()));
                        return true;
                    }
                    return false;
                })
                .map(ub -> {
                    ub.setPrice(ub.getPrice().subtract(orderRequestDTO.getAmount()));
                    userTransactionRepository.save(new UserTransaction(orderRequestDTO.getOrderId(),
                            orderRequestDTO.getUserId(),
                            orderEvent.getOrderRequestDTO().getAmount()));

                    return new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_FAILED));

    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        userTransactionRepository.findById(orderEvent.getOrderRequestDTO().getOrderId())
                .ifPresent(ut -> {
                    userTransactionRepository.delete(ut);
                    userTransactionRepository.findById(ut.getUserId())
                            .ifPresent(ub -> ub.setAmount(ub.getAmount().add(ut.getAmount())));
                });
    }
}
