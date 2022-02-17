package com.playtomic.tests.wallet.exception;

public class WalletNotFoundException extends RuntimeException {

    public static final String WALLET_NOT_FOUND_MESSAGE = "Wallet not found!";

    public WalletNotFoundException() {
        super(WALLET_NOT_FOUND_MESSAGE);
    }
}
