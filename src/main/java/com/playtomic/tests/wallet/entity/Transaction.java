package com.playtomic.tests.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Transaction extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String transactionId;

    @Column(precision = 13, scale = 5)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}
