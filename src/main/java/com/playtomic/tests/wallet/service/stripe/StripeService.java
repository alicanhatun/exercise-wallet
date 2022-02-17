package com.playtomic.tests.wallet.service.stripe;

import com.playtomic.tests.wallet.response.ChargeResponse;

import java.math.BigDecimal;

public interface StripeService {

    ChargeResponse charge(String creditCardNumber, BigDecimal amount);
}
