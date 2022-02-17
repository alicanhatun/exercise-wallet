package com.playtomic.tests.wallet.projection;

import java.math.BigDecimal;

public interface WalletProjection {

    String getIdentifier();

    void setIdentifier(String identifier);

    BigDecimal getBalance();

    void setBalance(BigDecimal balance);
}
