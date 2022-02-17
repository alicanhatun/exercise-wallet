package com.playtomic.tests.wallet.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Getter
@Setter
@ConfigurationProperties(prefix = "stripe.simulator")
public class StripeProperties {

    private URI chargesUri;

    private URI refundsUri;

}
