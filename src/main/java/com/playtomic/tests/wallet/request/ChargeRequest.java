package com.playtomic.tests.wallet.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public class ChargeRequest {

    @NotEmpty
    @JsonProperty("credit_card")
    String creditCardNumber;

    @NotNull
    @JsonProperty("amount")
    BigDecimal amount;
}
