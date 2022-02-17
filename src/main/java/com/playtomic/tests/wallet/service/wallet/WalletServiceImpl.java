package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.WalletResponse;
import com.playtomic.tests.wallet.service.stripe.StripeService;
import com.playtomic.tests.wallet.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Handles the wallet operations.
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final StripeService stripeService;

    private final TransactionService transactionService;

    /**
     * Finds wallet by identifier.
     *
     * @param identifier The value of the wallet
     * @return WalletResponse
     * @throws WalletNotFoundException when wallet not found by identifier
     */
    @Override
    public WalletResponse findByIdentifier(@NotBlank String identifier) {
        var walletProjection = walletRepository.findWalletProjectionByIdentifier(identifier)
                                               .orElseThrow(WalletNotFoundException::new);


        return new WalletResponse(walletProjection.getIdentifier(), walletProjection.getBalance());
    }

    /**
     * Charges wallet by identifier.
     *
     * @param identifier    The value of the wallet
     * @param ChargeRequest It has charging details
     * @throws WalletNotFoundException when wallet not found by identifier
     */
    @Override
    @Transactional
    public void chargeWallet(@NotBlank String identifier, ChargeRequest chargeRequest) {

        var wallet = walletRepository.findByIdentifier(identifier)
                                     .orElseThrow(WalletNotFoundException::new);

        var chargeResponse = stripeService.charge(chargeRequest.getCreditCardNumber(), chargeRequest.getAmount());
        //This is for breaking OptimisticLock.
        walletRepository.updateWalletBalanceByIdentifier(identifier, chargeRequest.getAmount());
        transactionService.createTransaction(wallet, chargeResponse);
    }


    /**
     * Creates wallet by identifier.
     *
     * @param identifier The value of the wallet
     */
    @Override
    public void createWallet(@NotBlank String identifier) {
        var wallet = new Wallet();
        wallet.setIdentifier(identifier);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);
    }
}
