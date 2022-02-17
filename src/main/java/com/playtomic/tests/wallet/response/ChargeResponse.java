package com.playtomic.tests.wallet.response;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class ChargeResponse {

    @NotEmpty
    String id;

    @NotNull
    BigDecimal amount;
}
