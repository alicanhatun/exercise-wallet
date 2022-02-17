package com.playtomic.tests.wallet.service.stripe;

import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.properties.StripeProperties;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.ChargeResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


/**
 * Handles the communication with Stripe.
 * <p>
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final RestTemplate restTemplate;

    private final StripeProperties stripeProperties;


    /**
     * Charges money in the credit card.
     * <p>
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param creditCardNumber The number of the credit card
     * @param amount           The amount that will be charged.
     * @return ChargeResponse
     * @throws StripeServiceException when exception occur
     */
    @Override
    public ChargeResponse charge(@NonNull String creditCardNumber, @NonNull BigDecimal amount) throws StripeServiceException {
        var body = new ChargeRequest(creditCardNumber, amount);
        return restTemplate.postForObject(stripeProperties.getChargesUri(), body, ChargeResponse.class);
    }

    /**
     * Refunds the specified payment.
     */
    private void refund(@NonNull String paymentId) throws StripeServiceException {
        // Object.class because we don't read the body here.
        restTemplate.postForEntity(stripeProperties.getChargesUri().toString(), null, Object.class, paymentId);
    }
}
