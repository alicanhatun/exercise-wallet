package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.properties.StripeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({StripeProperties.class})
public class PropertyConfig {

}
