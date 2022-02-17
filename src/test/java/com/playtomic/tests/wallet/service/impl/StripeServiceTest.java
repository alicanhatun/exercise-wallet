package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.properties.StripeProperties;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.ChargeResponse;
import com.playtomic.tests.wallet.service.stripe.StripeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test is failing with the current implementation.
 * <p>
 * How would you test this?
 */
@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StripeProperties stripeProperties;

    @InjectMocks
    private StripeServiceImpl stripeService;

    @Test
    void charge_correctInformation_verify() {

        String creditCard = "4242 4242 4242 4242";
        BigDecimal amount = new BigDecimal(5);
        var body = new ChargeRequest(creditCard, amount);

        stripeService.charge(creditCard, amount);

        verify(restTemplate, times(1)).postForObject(stripeProperties.getChargesUri(), body, ChargeResponse.class);
    }
}
