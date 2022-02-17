package com.playtomic.tests.wallet.service.transaction;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.response.ChargeResponse;

public interface TransactionService {

    void createTransaction(Wallet wallet, ChargeResponse chargeResponse);
}
