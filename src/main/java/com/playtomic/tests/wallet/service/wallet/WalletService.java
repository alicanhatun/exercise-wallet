package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.WalletResponse;

import javax.validation.constraints.NotBlank;

public interface WalletService {

    WalletResponse findByIdentifier(String identifier);

    void createWallet(@NotBlank String identifier);

    void chargeWallet(String identifier, ChargeRequest chargeRequest);
}
