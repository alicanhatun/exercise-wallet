package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.projection.WalletProjection;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.ChargeResponse;
import com.playtomic.tests.wallet.response.WalletResponse;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.transaction.TransactionService;
import com.playtomic.tests.wallet.service.wallet.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private StripeService stripeService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void findByIdentifier_found_returnWallet() {

        String identifier = "abc";

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        WalletProjection projection = factory.createProjection(WalletProjection.class);
        projection.setIdentifier(identifier);
        projection.setBalance(BigDecimal.ZERO);

        when(walletRepository.findWalletProjectionByIdentifier(identifier)).thenReturn(Optional.of(projection));

        WalletResponse walletResponse = walletService.findByIdentifier(identifier);

        Assertions.assertNotNull(walletResponse);
        Assertions.assertNotNull(walletResponse.getIdentifier());
        Assertions.assertNotNull(walletResponse.getBalance());
        Assertions.assertEquals(walletResponse.getIdentifier(), projection.getIdentifier());
        Assertions.assertEquals(walletResponse.getBalance(), projection.getBalance());
    }

    @Test
    void findByIdentifier_notFound_throwException() {

        String identifier = "abc";

        when(walletRepository.findWalletProjectionByIdentifier(identifier)).thenReturn(Optional.empty());

        Assertions.assertThrows(WalletNotFoundException.class, () -> walletService.findByIdentifier(identifier));
    }

    @Test
    void chargeWallet_notFound_throwException() {

        String identifier = "abc";
        ChargeRequest chargeRequest = new ChargeRequest(identifier, BigDecimal.ZERO);

        when(walletRepository.findByIdentifier(identifier)).thenReturn(Optional.empty());

        Assertions.assertThrows(WalletNotFoundException.class, () -> walletService.chargeWallet(identifier, chargeRequest));
        verify(walletRepository, never()).updateWalletBalanceByIdentifier(any(), any());
        verify(transactionService, never()).createTransaction(any(), any());
    }

    @Test
    void chargeWallet_found_verify() {

        String identifier = "abc";
        BigDecimal amount = BigDecimal.ZERO;
        ChargeRequest chargeRequest = new ChargeRequest(identifier, amount);
        ChargeResponse chargeResponse = new ChargeResponse(identifier, amount);
        Wallet wallet = new Wallet();

        when(walletRepository.findByIdentifier(identifier)).thenReturn(Optional.of(wallet));
        when(stripeService.charge(chargeRequest.getCreditCardNumber(), chargeRequest.getAmount())).thenReturn(chargeResponse);

        walletService.chargeWallet(identifier, chargeRequest);

        verify(walletRepository, times(1)).updateWalletBalanceByIdentifier(identifier, chargeRequest.getAmount());
        verify(transactionService, times(1)).createTransaction(wallet, chargeResponse);
    }
}
