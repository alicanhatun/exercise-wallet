package com.playtomic.tests.wallet.service.transaction;

import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.repository.TransactionRepository;
import com.playtomic.tests.wallet.response.ChargeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Handles the transactions.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Creates transaction.
     *
     * @param Wallet         The value of the wallet
     * @param ChargeResponse It has charging details
     */
    @Override
    public void createTransaction(Wallet wallet, ChargeResponse chargeResponse) {
        var transaction = new Transaction();
        transaction.setTransactionId(chargeResponse.getId());
        transaction.setAmount(chargeResponse.getAmount());
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);
    }
}
