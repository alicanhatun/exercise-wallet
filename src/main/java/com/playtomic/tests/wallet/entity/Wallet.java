package com.playtomic.tests.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
public class Wallet extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(precision = 13, scale = 5)
    private BigDecimal balance;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Transaction> transactions;
}
