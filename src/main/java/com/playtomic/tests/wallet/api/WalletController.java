package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.WalletResponse;
import com.playtomic.tests.wallet.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{identifier}")
    public HttpEntity<WalletResponse> getWallet(@PathVariable String identifier) {
        return new HttpEntity<>(walletService.findByIdentifier(identifier));
    }

    @PostMapping("/{identifier}")
    public HttpStatus createWallet(@PathVariable String identifier) {
        walletService.createWallet(identifier);
        return HttpStatus.OK;
    }

    @PutMapping("/charge/{identifier}")
    public HttpStatus chargeWallet(@PathVariable String identifier, @RequestBody @Valid ChargeRequest chargeRequest) {
        walletService.chargeWallet(identifier, chargeRequest);
        return HttpStatus.OK;
    }
}
