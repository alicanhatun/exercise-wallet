package com.playtomic.tests.wallet.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {

    private String identifier;

    private BigDecimal balance;
}
